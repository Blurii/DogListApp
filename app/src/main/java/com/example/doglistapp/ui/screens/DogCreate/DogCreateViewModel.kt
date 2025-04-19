package com.example.doglistapp.ui.screens.DogCreate

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.doglistapp.DoggoApplication
import com.example.doglistapp.data.DogsRepository
import com.example.doglistapp.model.DogPhoto
import kotlinx.coroutines.launch

class DogCreateViewModel(
    private val dogsPhotosRepository: DogsRepository
) : ViewModel() {

    sealed interface UiState {
        data class Success(val photo: DogPhoto): UiState
        object Error: UiState
        object Loading: UiState
    }

    var uiState: UiState by mutableStateOf(UiState.Loading)
        private set

    init {
        getDogImage()
    }

    fun getDogImage() {
        viewModelScope.launch {
            uiState = UiState.Loading
            uiState = try {
                val image = dogsPhotosRepository.getRandomDogImage()
                UiState.Success(image)
            } catch (e: Exception) {
                Log.e("DogCreateViewModel", "Błąd pobierania obrazka", e)
                UiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DoggoApplication)
                val dogsPhotosRepository = application.container.dogsPhotosRepository
                DogCreateViewModel(dogsPhotosRepository)
            }
        }
    }
}