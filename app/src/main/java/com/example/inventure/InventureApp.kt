package com.example.inventure

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventureApp(viewModel: InventureViewModel) {
    val inventure by viewModel.inventure.collectAsState()
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "welcome") {

        composable("welcome") { WelcomeScreen(viewModel, navController) }

        composable("auth") {
            AuthScreen(onLoginSuccess = { navController.navigate("home") })
        }

        composable("home") {
            HomeScreen(viewModel, navController)
        }

        composable("products") { ProductListScreen(viewModel, navController) }
        composable("add_stock") { AddStockScreen(viewModel, navController) }
        composable("remove_stock") { RemoveStockScreen(viewModel, navController) }
    }
}

@Preview(showBackground = true)
@Composable
fun DisplayUX() {
    val fakeViewModel = InventureViewModel()
    InventureApp(viewModel = fakeViewModel)
}
