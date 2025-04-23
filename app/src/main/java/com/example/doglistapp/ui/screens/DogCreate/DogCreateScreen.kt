package com.example.doglistapp.ui.screens.DogCreate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.doglistapp.model.DogList
import com.example.doglistapp.ui.screens.DogDetails.ErrorScreen
import com.example.doglistapp.ui.screens.DogDetails.LoadingScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogCreateScreen(
    navController: NavController,
    viewModel: DogCreateViewModel,
    retryAction: () -> Unit
) {

    val name by viewModel.name
    val breed by viewModel.breed
    val dogAdded = viewModel.dogAdded.value

    LaunchedEffect(dogAdded) {
        if (dogAdded) {
            navController.navigate(DogList)
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Dodaj Psa") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(DogList) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFFEF7FF)
                ),
                modifier = Modifier.statusBarsPadding()
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(top = 32.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .height(220.dp)
                        .width(320.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    when (val uiState = viewModel.uiState) {
                        is DogCreateViewModel.UiState.Loading -> LoadingScreen()
                        is DogCreateViewModel.UiState.Error -> ErrorScreen(
                            retryAction = retryAction
                        )

                        is DogCreateViewModel.UiState.Success -> {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(uiState.photo.message.replace("http://", "https://"))
                                    .crossfade(true)
                                    .size(220, 320)
                                    .build(),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { viewModel.name.value = it },
                    label = { Text("Imię") },
                    singleLine = true,
                    modifier = Modifier
                        .width(320.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = breed,
                    onValueChange = { viewModel.breed.value = it },
                    label = { Text("Rasa") },
                    singleLine = true,
                    modifier = Modifier
                        .width(320.dp)
                )

                Spacer(modifier = Modifier.height(72.dp))

                if (viewModel.showError.value) {
                    Text(
                        text = "Piesek już istnieje!",
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .width(320.dp)
                        .height(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xff65558f), Color(0xf0eeb6e8)),
                                start = Offset(0f, 0f),
                                end = Offset.Infinite
                            )
                        )
                ) {
                    Button(
                        onClick = {
                            if (name.isNotEmpty() && breed.isNotEmpty()) {
                                viewModel.addDog(name, breed)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("Add", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}