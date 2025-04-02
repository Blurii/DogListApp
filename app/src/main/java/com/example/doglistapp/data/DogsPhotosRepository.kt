package com.example.doglistapp.data

import com.example.doglistapp.model.DogPhoto
import com.example.doglistapp.data.network.DogsService

interface DogsPhotosRepository {
    suspend fun getRandomDogImage() : DogPhoto
}

class  NetworkDogsPhotosRepository(
    private val dogsService: DogsService,
) : DogsPhotosRepository{

    override suspend fun getRandomDogImage(): DogPhoto = dogsService.getRandomDogImage()
}
