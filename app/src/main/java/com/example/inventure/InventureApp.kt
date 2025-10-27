package com.example.inventure

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventure.ui.AddStockScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

// ✅ Application class that initializes database and repository
class InventureApplication : Application() {
    val database: InventureDatabase by lazy { InventureDatabase.getDatabase(this) }
    val repository: ProductRepository by lazy { ProductRepository(database.inventureDao()) }
}

@Composable
fun InventureApp(viewModel: InventureViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        // Welcome Screen
        composable("welcome") {
            WelcomeScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        // Auth Screen
        composable("auth") {
            AuthScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }

        // Home Screen
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        // Product List Screen
        composable("products") {
            ProductListScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Add Stock Screen
        composable("add_stock") {
            AddStockScreen(
                viewModel = viewModel,
                onAddSuccess = { navController.popBackStack() }
            )
        }

        // Remove Stock Screen (placeholder - you'll need to create this)
        composable("remove_stock") {
            // TODO: Create RemoveStockScreen composable
            ProductListScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

// ✅ Fake DAO for Preview
class FakeInventureDao : InventureDao {
    override fun getAllProducts(): Flow<List<Inventure>> = flowOf(
        listOf(
            Inventure(1, "Sample Product", "Test description", 1000.0, 10),
            Inventure(2, "Another Item", "More details", 2500.0, 5)
        )
    )
    override suspend fun insertProduct(product: Inventure) {}
    override suspend fun deleteProduct(product: Inventure) {}
}

@Preview(showBackground = true)
@Composable
fun DisplayUX() {
    val fakeDao = FakeInventureDao()
    val fakeRepository = ProductRepository(fakeDao)
    val fakeViewModel = InventureViewModel(fakeRepository)

    InventureApp(viewModel = fakeViewModel)
}