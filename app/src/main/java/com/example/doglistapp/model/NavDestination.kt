package com.example.doglistapp.model

import  kotlinx.serialization.Serializable

@Serializable
object DogList

@Serializable
data class DogScreen(val name: String)