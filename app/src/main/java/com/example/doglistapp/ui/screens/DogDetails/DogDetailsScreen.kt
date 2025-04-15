package com.example.doglistapp.ui
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.doglistapp.model.DogList
import com.example.doglistapp.ui.screens.DogDetails.DogDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogDetailsScreen(
    dogName: String,
    dogBreed: String,
    navController: NavController,
    uiState: DogDetailsViewModel.UiState,
    retryAction: () -> Unit,
    photoUrl: String,
) {

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Detale")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(DogList) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFFEF7FF)
                ),
                modifier = Modifier.statusBarsPadding()
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Box(
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                ContentScreen(uiState = uiState, retryAction = retryAction, initialPhotoUrl = photoUrl)
            }

            Spacer(modifier = Modifier.height(64.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = dogName,
                    fontSize = 22.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = dogBreed,
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun ContentScreen(uiState: DogDetailsViewModel.UiState, retryAction: () -> Unit, initialPhotoUrl: String, modifier: Modifier = Modifier) {
    when(uiState) {
        is DogDetailsViewModel.UiState.Loading -> LoadingScreen(
            modifier = modifier
        )
        is DogDetailsViewModel.UiState.Error -> ErrorScreen(
            retryAction = retryAction, modifier = modifier
        )
        is DogDetailsViewModel.UiState.Success -> {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(initialPhotoUrl)
                    .crossfade(true)
                    .size(900, 900)
                    .build(),
                contentDescription = null
            )
        }

    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier.width(64.dp),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = Icons.Default.Warning, contentDescription = ""
        )
        Text(text = "Failed", modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Image(imageVector = Icons.Default.Refresh, contentDescription = null)
        }
    }
}