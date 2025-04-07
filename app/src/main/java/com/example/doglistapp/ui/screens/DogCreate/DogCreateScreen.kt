package com.example.doglistapp.ui.screens.DogCreate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.doglistapp.ui.ErrorScreen
import com.example.doglistapp.ui.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogCreateScreen(
    navController: NavController,
    onDogAdded: (String, String, String) -> Unit,
    viewModel: DogCreateViewModel,
    retryAction: () -> Unit
) {
    val name = remember { mutableStateOf("") }
    val breed = remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Dodaj Psa") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text("Imię") },
                    modifier = Modifier
                        .width(320.dp)
                        .height(56.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = breed.value,
                    onValueChange = { breed.value = it },
                    label = { Text("Rasa") },
                    modifier = Modifier
                        .width(320.dp)
                        .height(56.dp)
                )

                Spacer(modifier = Modifier.height(72.dp))

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
                            if (name.value.isNotBlank() && breed.value.isNotBlank()) {
                                onDogAdded(name.value, breed.value, (viewModel.uiState as? DogCreateViewModel.UiState.Success)?.photo?.message ?: "")

                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(),
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Text("Add", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
