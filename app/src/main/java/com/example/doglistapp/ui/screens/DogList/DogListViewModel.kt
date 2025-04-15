package com.example.doglistapp.ui.screens.DogList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.example.doglistapp.model.Dog

class DogListViewModel : ViewModel() {

    var searchText by mutableStateOf(TextFieldValue(""))
        private set

    fun onSearchTextChange(newValue: TextFieldValue) {
        searchText = newValue
    }

    fun getFilteredDogs(
        dogList: List<Dog>,
        favoriteDogs: Set<String>
    ): List<Dog> {
        return if (searchText.text.isNotBlank()) {
            dogList.filter { it.name.contains(searchText.text, ignoreCase = true) }
        } else {
            dogList.sortedByDescending { favoriteDogs.contains(it.name) }
        }
    }
}