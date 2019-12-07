package com.prattham.dogsapp.model

import io.reactivex.Single
import retrofit2.http.GET

interface DogsAPi {
    @GET("DevTides/DogsApi/master/dogs.json")
    fun getDogs(): Single<List<DogBreed>>
}