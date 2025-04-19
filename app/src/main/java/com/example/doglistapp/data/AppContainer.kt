package com.example.doglistapp.data

import android.content.Context
import androidx.room.Room
import com.example.doglistapp.data.local.database.AppDatabase
import com.example.doglistapp.data.local.database.DogEntityDao
import com.example.doglistapp.data.network.DogsService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val dogsRepository: DogsRepository
    val dogDao: DogEntityDao
}

class DefaultAppContainer(context: Context) : AppContainer {
    private val dogsApiBaseUrl = "https://dog.ceo/api/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(dogsApiBaseUrl)
        .build()

    private val dogsService: DogsService by lazy {
        retrofit.create(DogsService::class.java)
    }

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()
    }

    override val dogDao: DogEntityDao by lazy {
        database.dogDao()
    }

    override val dogsRepository: DogsRepository by lazy {
        DefaultDogsRepository(dogsService, dogDao)
    }

}