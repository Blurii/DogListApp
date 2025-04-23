package com.example.doglistapp.ui.screens.DogCreate

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.doglistapp.DoggoApplication
import com.example.doglistapp.data.DogsRepository
import com.example.doglistapp.model.DogPhoto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DogCreateViewModel(
    private val dogsRepository: DogsRepository
) : ViewModel() {

    sealed interface UiState {
        data class Success(val photo: DogPhoto): UiState
        object Error: UiState
        object Loading: UiState
    }

    val name = mutableStateOf("")
    val breed = mutableStateOf("")
    val showError = mutableStateOf(false)
    val dogAdded = mutableStateOf(false)


    var uiState: UiState by mutableStateOf(UiState.Loading)
        private set

    init {
        getDogImage()
    }

    fun getDogImage() {
        viewModelScope.launch {
            uiState = UiState.Loading
            uiState = try {
                val image = dogsRepository.getRandomDogImage()
                UiState.Success(image)
            } catch (e: Exception) {
                Log.e("DogCreateViewModel", "Błąd pobierania obrazka", e)
                UiState.Error
            }
        }
    }

    fun addDog(name: String, breed: String) {
        viewModelScope.launch {

            val existingDogs = dogsRepository.dogs.first()
            val alreadyExists = existingDogs.any { it.name.equals(name, ignoreCase = true) }

            if (alreadyExists) {
                showError.value = true
                dogAdded.value = false
                return@launch
            }

            showError.value = false

            val imageUrl = when (val state = uiState) {
                is UiState.Success -> state.photo.message.replace("http://", "https://")
                else -> ""
            }

            dogsRepository.add(name, breed, imageUrl)

            dogAdded.value = true

            this@DogCreateViewModel.name.value = ""
            this@DogCreateViewModel.breed.value = ""
        }
    }



    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as DoggoApplication)
                val dogsRepository = application.container.dogsRepository
                DogCreateViewModel(dogsRepository)
            }
        }
    }
}
