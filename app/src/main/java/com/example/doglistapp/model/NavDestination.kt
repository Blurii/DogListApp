package com.example.doglistapp.model

import  kotlinx.serialization.Serializable

@Serializable
object DogList

@Serializable
object DogSettings

@Serializable
object DogProfile

@Serializable
data class DogDetails(val name: String, val breed: String, val imageUrl: String)

@Serializable
object DogCreate