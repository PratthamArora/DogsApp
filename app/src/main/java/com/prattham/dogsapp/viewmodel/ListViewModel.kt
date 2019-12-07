package com.prattham.dogsapp.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.prattham.dogsapp.model.DogApiService
import com.prattham.dogsapp.model.DogBreed
import com.prattham.dogsapp.model.DogDatabase
import com.prattham.dogsapp.util.NotificationHelper
import com.prattham.dogsapp.util.SharedPrefsHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : BaseViewModel(application) {

    private var prefHelper = SharedPrefsHelper(getApplication())
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L //in nanoseconds
    private val dogsService = DogApiService()
    private val disposable = CompositeDisposable()

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {

        checkCacheDuration()

        val updateTime = prefHelper.getUpdateTime()

        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDatabase()
        } else {
            fetchFromRemote()
        }
    }

    private fun checkCacheDuration() {
        val cachePref = prefHelper.getCacheDuration()

        try {
            val cachePrefInt = cachePref?.toInt() ?: 5 * 60
            refreshTime = cachePrefInt.times(1000 * 1000 * 1000L)

        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    private fun fetchFromDatabase() {
        loading.value = true
        launch {
            val dogs = DogDatabase(getApplication()).dogDao().getAllDogs()
            dogsRetrieved(dogs)
            Toast.makeText(getApplication(), "Data Retrieved from Database", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun fetchFromRemote() {
        loading.value = true
        disposable.add(
            dogsService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>() {
                    override fun onSuccess(dogsList: List<DogBreed>) {

                        storeDataLocally(dogsList)
                        Toast.makeText(
                            getApplication(),
                            "Data Retrieved from API",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        NotificationHelper(getApplication()).createNotification()

                    }

                    override fun onError(e: Throwable) {
                        loading.value = true
                        dogLoadError.value = true
                        e.printStackTrace()
                    }

                })
        )

    }

    fun refreshBypassCache() {
        fetchFromRemote()
    }

    private fun storeDataLocally(list: List<DogBreed>) {
        launch {
            val dao = DogDatabase(getApplication()).dogDao()
            dao.deleteAllDogs()
            val result = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uuid = result[i].toInt()
                ++i
            }
            dogsRetrieved(list)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    private fun dogsRetrieved(dogsList: List<DogBreed>) {
        dogs.value = dogsList
        loading.value = false
        dogLoadError.value = false
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}