package com.example.doglistapp.model

import  kotlinx.serialization.Serializable

@Serializable
object DogList

@Serializable
object DogSettings

@Serializable
object DogProfile

@Serializable
data class DogDetails(
    val dogName: String,
    val dogBreed: String,
    val photoUrl: String
)

@Serializable
object DogCreate