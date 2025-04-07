package com.example.doglistapp.data.network

import retrofit2.http.GET
import com.example.doglistapp.model.DogPhoto

interface DogsService {

    @GET("breeds/image/random")
    suspend fun  getRandomDogImage(): DogPhoto
}