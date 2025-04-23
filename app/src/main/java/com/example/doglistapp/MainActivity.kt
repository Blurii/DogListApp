package com.example.doglistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.doglistapp.model.DogCreate
import com.example.doglistapp.model.DogDetails
import com.example.doglistapp.model.DogList
import com.example.doglistapp.model.DogProfile
import com.example.doglistapp.model.DogSettings
import com.example.doglistapp.ui.screens.DogCreate.DogCreateScreen
import com.example.doglistapp.ui.screens.DogCreate.DogCreateViewModel
import com.example.doglistapp.ui.screens.DogDetails.DogDetailsScreen
import com.example.doglistapp.ui.screens.DogDetails.DogDetailsViewModel
import com.example.doglistapp.ui.screens.DogList.DogListViewModel
import com.example.doglistapp.ui.screens.DogList.DogsScreen
import com.example.doglistapp.ui.screens.DogProfile.DogProfileScreen
import com.example.doglistapp.ui.screens.DogSettings.DogSettingsScreen
import com.example.doglistapp.ui.theme.DogListAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DogListAppTheme {
                val navigationController = rememberNavController()
                NavHost(navController = navigationController, startDestination = DogList) {
                    composable<DogList> {
                        val viewModel: DogListViewModel =
                            viewModel(factory = DogListViewModel.Factory)
                        DogsScreen(
                            viewModel = viewModel,
                            navigationController = navigationController
                        )
                    }
                    composable<DogDetails> {
                        val args = it.toRoute<DogDetails>()
                        val viewModel: DogDetailsViewModel =
                            viewModel(factory = DogDetailsViewModel.Factory)
                        DogDetailsScreen(
                            args,
                            viewModel.uiState,
                            viewModel::getDogImage,
                            navigationController
                        )
                    }
                    composable<DogSettings> {
                        DogSettingsScreen(navController = navigationController)
                    }
                    composable<DogProfile> {
                        DogProfileScreen(navController = navigationController)
                    }
                    composable<DogCreate> {
                        val viewModel: DogCreateViewModel =
                            viewModel(factory = DogCreateViewModel.Factory)
                        DogCreateScreen(
                            viewModel = viewModel,
                            navController = navigationController,
                            retryAction = viewModel::getDogImage
                        )
                    }
                }
            }
        }
    }
}