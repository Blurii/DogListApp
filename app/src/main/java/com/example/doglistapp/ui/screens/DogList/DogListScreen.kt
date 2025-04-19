package com.example.doglistapp.ui.screens.DogList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.doglistapp.model.Dog
import com.example.doglistapp.model.DogCreate
import com.example.doglistapp.model.DogDetails
import com.example.doglistapp.model.DogProfile
import com.example.doglistapp.model.DogSettings
import com.example.doglistapp.ui.components.DogItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogListScreen(
    navController: NavHostController,
    dogList: SnapshotStateList<Dog>,
    favoriteDogs: Set<String>,
    onToggleFavorite: (String) -> Unit,
    onDelete: (String) -> Unit,
    viewModel: DogListViewModel = remember { DogListViewModel() }
) {
    val searchText = viewModel.searchText
    val filteredDogs = viewModel.getFilteredDogs(dogList, favoriteDogs)

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Doggos") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(DogSettings) }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(DogProfile) }) {
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
                        onValueChange = { viewModel.onSearchTextChange(it) },
                        label = { Text("Wyszukaj pieska 🐕") },
                        modifier = Modifier.weight(1f),
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = { navController.navigate(DogCreate) },
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .offset(y = 4.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Text(text = "🐶: ${dogList.size}")
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "❤️: ${favoriteDogs.size}")
                }

                Spacer(modifier = Modifier.height(8.dp))

                filteredDogs.forEach { dog ->
                    DogItem(
                        name = dog.name,
                        breed = dog.breed,
                        isFavorite = favoriteDogs.contains(dog.name),
                        onFavoriteToggle = { onToggleFavorite(dog.name) },
                        onDelete = { onDelete(dog.name) },
                        onClick = {
                            navController.navigate(
                                DogDetails(
                                    dogName = dog.name,
                                    dogBreed = dog.breed,
                                    photoUrl = dog.photoUrl
                                )
                            )
                        },
                        photoUrl = dog.photoUrl
                    )
                }
            }
        }
    )
}