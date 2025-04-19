package com.example.doglistapp.model

data class Dog(
    val id: Int,
    val name: String,
    val breed: String,
    val photoUrl: String,
    val isFavorite: Boolean,
)