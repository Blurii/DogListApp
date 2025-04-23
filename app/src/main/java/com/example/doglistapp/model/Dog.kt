package com.example.doglistapp.model

data class Dog(
    val id: Int,
    val name: String,
    val breed: String,
    val isFavorite: Boolean,
    val imageUrl: String
)