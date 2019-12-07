package com.prattham.dogsapp.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.prattham.dogsapp.model.DogBreed
import com.prattham.dogsapp.model.DogDatabase
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : BaseViewModel(application) {
    val dogLiveData = MutableLiveData<DogBreed>()

    fun fetch(uuid: Int) {
        launch {
            val dog = DogDatabase(getApplication()).dogDao().getDog(uuid)
            dogLiveData.value = dog
        }

    }
}