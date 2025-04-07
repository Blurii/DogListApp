package com.example.doglistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.doglistapp.ui.DogList
import com.example.doglistapp.ui.DogDetailsScreen
import com.example.doglistapp.ui.screens.DogProfile.DogProfileScreen
import com.example.doglistapp.ui.screens.DogSettings.DogSettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            val dogList = remember { mutableStateListOf<Pair<String, String>>() }
            val favoriteDogs = remember { mutableStateListOf<String>() }

            NavHost(navController = navController, startDestination = "dog_list") {
                composable("dog_list") {
                    DogList(
                        navController = navController,
                        dogList = dogList,
                        favoriteDogs = favoriteDogs.toSet(),
                        onAddDog = { name ->
                            dogList.add(name to "Nieznana rasa")
                        },
                        onToggleFavorite = { name ->
                            if (favoriteDogs.contains(name)) {
                                favoriteDogs.remove(name)
                            } else {
                                favoriteDogs.add(name)
                            }
                        },
                        onDelete = { name ->
                            dogList.removeAll { it.first == name }
                            favoriteDogs.remove(name)
                        }
                    )
                }

                composable("dog_details/{dogName}") { backStackEntry ->
                    val dogName = backStackEntry.arguments?.getString("dogName") ?: "Unknown"
                    DogDetailsScreen(
                        dogName = dogName,
                        navController = navController,
                        onDelete = { name ->
                            dogList.removeAll { it.first == name }
                            favoriteDogs.remove(name)
                        }
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
            }
        }
    }
}
