package com.example.doglistapp.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.doglistapp.DogDetailsScreen
import com.example.doglistapp.DogItem
import com.example.doglistapp.ProfileScreen
import com.example.doglistapp.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogList(
name: String
) {


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        when (currentRoute) {
                            "profile" -> "Profil"
                            "dog_details/{dogName}" -> "Detale"
                            "settings" -> "Ustawienia"
                            else -> "Doggos"
                        },
                        color = Color.Black,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                },
                navigationIcon = {
                    Row {
                        if (currentRoute == "settings" || currentRoute == "profile" || currentRoute.startsWith("dog_details")) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.Black
                                )
                            }
                        } else {
                            IconButton(onClick = { navController.navigate("settings") }) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings",
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                },
                actions = {
                    if (currentRoute.startsWith("dog_details/")) {
                        val dogName = currentRoute.substringAfter("dog_details/")
                        IconButton(onClick = {
                            removeDog(dogName)
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.Red
                            )
                        }
                    } else if (currentRoute != "profile" && currentRoute != "settings") {
                        IconButton(onClick = { navController.navigate("profile") }) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Profile",
                                tint = Color.Black
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFFEF7FF)
                ),
                modifier = Modifier.statusBarsPadding()
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "dog_list",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("dog_list") {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.White)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = searchText,
                            onValueChange = {
                                searchText = it
                                showError = false
                                isSearching = false
                            },
                            label = { Text("Poszukaj lub dodaj pieska 🐕") },
                            modifier = Modifier.weight(1f),
                            isError = showError
                        )


                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(onClick = { isSearching = true },
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .offset(y = 4.dp)
                        ) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }


                        IconButton(onClick = {

                            if (searchText.text.isNotBlank()) {
                                if (dogList.any { it.first == searchText.text }) {
                                    showError = true
                                } else {
                                    dogList.add(searchText.text to "Jack Russel")
                                    searchText = TextFieldValue("")
                                }
                            }
                        },
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .offset(y = 4.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        }
                    }

                    if (showError) {
                        Text(text = "Piesek już istnieje!", color = Color.Red, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Text(text = "🐶: ${dogList.size}")
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "❤️: ${favoriteDogs.size}")
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    filteredDogs.forEach { (name, breed) ->
                        DogItem(
                            name = name,
                            breed = breed,
                            isFavorite = favoriteDogs.contains(name),
                            onFavoriteToggle = {
                                favoriteDogs = if (favoriteDogs.contains(name)) {
                                    favoriteDogs - name
                                } else {
                                    favoriteDogs + name
                                }
                            },
                            onDelete = {
                                dogList.removeAll { it.first == name }
                                favoriteDogs = favoriteDogs - name
                            },
                            onClick = {
                                navController.navigate("dog_details/$name")
                            }
                        )
                    }
                }
            }

            composable("dog_details/{dogName}") { backStackEntry ->
                val dogName = backStackEntry.arguments?.getString("dogName") ?: "Unknown"
                DogDetailsScreen(
                    dogName = dogName,
                    navController = navController,
                    onDelete = { name ->
                        dogList.removeAll { it.first == name }
                        favoriteDogs = favoriteDogs - name
                    }
                )
            }

            composable("profile") {
                ProfileScreen()
            }
            composable("settings") {
                SettingsScreen()
            }
        }
    }
}