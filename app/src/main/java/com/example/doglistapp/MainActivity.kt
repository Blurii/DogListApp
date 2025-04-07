package com.example.doglistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.doglistapp.model.Dog
import com.example.doglistapp.ui.DogList
import com.example.doglistapp.ui.DogDetailsScreen
import com.example.doglistapp.ui.screens.DogCreate.DogCreateScreen
import com.example.doglistapp.ui.screens.DogCreate.DogCreateViewModel
import com.example.doglistapp.ui.screens.DogDetails.DogDetailsViewModel
import com.example.doglistapp.ui.screens.DogProfile.DogProfileScreen
import com.example.doglistapp.ui.screens.DogSettings.DogSettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            val dogList = remember { mutableStateListOf<Dog>() }
            val favoriteDogs = remember { mutableStateListOf<String>() }

            NavHost(navController = navController, startDestination = "dog_list") {
                composable("dog_list") {
                    DogList(
                        navController = navController,
                        dogList = dogList,
                        favoriteDogs = favoriteDogs.toSet(),
                        onAddDog = { name ->
                            dogList.add(Dog(name, "Nieznana rasa", ""))
                        },
                        onToggleFavorite = { name ->
                            if (favoriteDogs.contains(name)) {
                                favoriteDogs.remove(name)
                            } else {
                                favoriteDogs.add(name)
                            }
                        },
                        onDelete = { name ->
                            dogList.removeAll { it.name == name }
                            favoriteDogs.remove(name)
                        }
                    )
                }
                composable("dog_details/{dogName}/{dogBreed}/{photoUrl}") { backStackEntry ->
                    val dogName = backStackEntry.arguments?.getString("dogName") ?: "Unknown"
                    val dogBreed = backStackEntry.arguments?.getString("dogBreed") ?: "Unknown"
                    val photoUrlEncoded = backStackEntry.arguments?.getString("photoUrl") ?: ""
                    val photoUrl = java.net.URLDecoder.decode(photoUrlEncoded, "UTF-8")

                    val viewModel: DogDetailsViewModel = viewModel(factory = DogDetailsViewModel.Factory)
                    DogDetailsScreen(
                        dogName = dogName,
                        dogBreed = dogBreed,
                        photoUrl = photoUrl,
                        navController = navController,
                        onDelete = { name ->
                            dogList.removeAll { it.name == name }
                            favoriteDogs.remove(name)
                        },
                        uiState = viewModel.uiState,
                        retryAction = viewModel::getDogImage
                    )
                }
                composable("settings") {
                    DogSettingsScreen(
                        navController = navController
                    )
                }
                composable("profile") {
                    DogProfileScreen(navController)
                }
                composable("dog_add") {
                    val viewModel: DogCreateViewModel =
                        viewModel(factory = DogCreateViewModel.Factory)
                    DogCreateScreen(
                        navController = navController,
                        onDogAdded = { name, breed, photoUrl ->
                            dogList.add(Dog(name, breed, photoUrl))
                            navController.popBackStack()
                        },
                        viewModel = viewModel,
                        retryAction = viewModel::getDogImage
                    )
                }
            }
        }
    }
}
