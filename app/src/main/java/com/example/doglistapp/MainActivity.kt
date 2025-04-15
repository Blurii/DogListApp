package com.example.doglistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.doglistapp.model.Dog
import com.example.doglistapp.model.DogCreate
import com.example.doglistapp.model.DogDetails
import com.example.doglistapp.model.DogList
import com.example.doglistapp.model.DogProfile
import com.example.doglistapp.model.DogSettings
import com.example.doglistapp.ui.DogDetailsScreen
import com.example.doglistapp.ui.screens.DogCreate.DogCreateScreen
import com.example.doglistapp.ui.screens.DogCreate.DogCreateViewModel
import com.example.doglistapp.ui.screens.DogDetails.DogDetailsViewModel
import com.example.doglistapp.ui.screens.DogList.DogListScreen
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

            NavHost(navController = navController, startDestination = DogList) {
                composable<DogList> {
                    DogListScreen(
                        navController = navController,
                        dogList = dogList,
                        favoriteDogs = favoriteDogs.toSet(),
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
                composable<DogDetails> { backStackEntry ->
                    val args = backStackEntry.toRoute<DogDetails>()
                    val decodedUrl = java.net.URLDecoder.decode(args.photoUrl, "UTF-8")

                    val viewModel: DogDetailsViewModel = viewModel(factory = DogDetailsViewModel.Factory)
                    DogDetailsScreen(
                        dogName = args.dogName,
                        dogBreed = args.dogBreed,
                        photoUrl = decodedUrl,
                        navController = navController,
                        uiState = viewModel.uiState,
                        retryAction = viewModel::getDogImage
                    )
                }
                composable<DogSettings> {
                    DogSettingsScreen(
                        navController = navController
                    )
                }
                composable<DogProfile> {
                    DogProfileScreen(navController)
                }
                composable<DogCreate> {
                    val viewModel: DogCreateViewModel =
                        viewModel(factory = DogCreateViewModel.Factory)
                    DogCreateScreen(
                        navController = navController,
                        onDogAdded = { name, breed, photoUrl ->
                            dogList.add(Dog(name, breed, photoUrl))
                            navController.popBackStack()
                        },
                        viewModel = viewModel,
                        retryAction = viewModel::getDogImage,
                        existingDogs = dogList
                    )
                }
            }
        }
    }
}
