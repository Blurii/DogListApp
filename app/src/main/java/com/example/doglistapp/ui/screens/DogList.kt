package com.example.doglistapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.doglistapp.ui.components.DogItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogList(
    navController: NavHostController,
    dogList: MutableList<Pair<String, String>>,
    favoriteDogs: Set<String>,
    onAddDog: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onDelete: (String) -> Unit,
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var showError by remember { mutableStateOf(false) }
    var isSearching by remember { mutableStateOf(false) }

    val filteredDogs = if (searchText.text.isNotBlank()) {
        dogList.filter { it.first.contains(searchText.text, ignoreCase = true) }
    } else {
        dogList.sortedByDescending { favoriteDogs.contains(it.first) }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Doggos")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(Icons.Filled.Person, contentDescription = "Profile")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFFEF7FF)
                ),
                modifier = Modifier.statusBarsPadding()
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                            showError = false
                        },
                        label = { Text("Wyszukaj pieska 🐕") },
                        modifier = Modifier.weight(1f),
                        isError = showError
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = {
                        if (searchText.text.isNotBlank()) {
                            if (dogList.any { it.first == searchText.text }) {
                                showError = true
                            } else {
                                onAddDog(searchText.text)
                                searchText = TextFieldValue("")
                            }
                        }
                    }) {
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
                        onFavoriteToggle = { onToggleFavorite(name) },
                        onDelete = { onDelete(name) },
                        onClick = { navController.navigate("dog_details/$name") }
                    )
                }
            }
        }
    )
}