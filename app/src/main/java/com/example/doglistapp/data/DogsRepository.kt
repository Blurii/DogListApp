package com.example.doglistapp.data

import com.example.doglistapp.data.local.database.DogEntity
import com.example.doglistapp.data.local.database.DogEntityDao
import com.example.doglistapp.data.network.DogsService
import com.example.doglistapp.model.Dog
import com.example.doglistapp.model.DogPhoto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface DogsRepository {
    val dogs: Flow<List<Dog>>

    suspend fun getRandomDogImage(): DogPhoto
    suspend fun add(name: String, breed: String, imageUrl: String)
    suspend fun remove(id: Int)
    suspend fun triggerFav(id: Int)
}

class DefaultDogsRepository(
    private val dogsService: DogsService,
    private val dogDao: DogEntityDao
) : DogsRepository {

    override val dogs: Flow<List<Dog>> = dogDao.getSortedDogs().map { items ->
        items.map {
            Dog(
                id = it.uid,
                name = it.name,
                breed = it.breed,
                isFavorite = it.isFav,
                imageUrl = it.imageUrl
            )
        }
    }

    override suspend fun getRandomDogImage(): DogPhoto = dogsService.getRandomDogImage()

    override suspend fun add(name: String, breed: String, imageUrl: String) {
        dogDao.insertDog(DogEntity(name = name, breed = breed, isFav = false, imageUrl = imageUrl))
    }

    override suspend fun remove(id: Int) {
        dogDao.removeDog(id)
    }

    override suspend fun triggerFav(id: Int) {
        dogDao.triggerFavDog(id)
    }
}