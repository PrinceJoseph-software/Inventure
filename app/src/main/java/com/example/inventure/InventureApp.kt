package com.example.inventure

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventure.ui.AddStockScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class InventureApplication : Application() {
    val database: InventureDatabase by lazy { InventureDatabase.getDatabase(this) }
    val repository: ProductRepository by lazy { ProductRepository(database.inventureDao()) }
}

@Composable
fun InventureApp(
    viewModel: InventureViewModel,
    authManager: AuthManager,
    isDarkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {
        composable("welcome") {
            WelcomeScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable("auth") {
            AuthScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                navController = navController,
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode,
                onLogout = {
                    scope.launch {
                        authManager.logout()
                        navController.navigate("auth") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable("products") {
            ProductListScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("add_stock") {
            AddStockScreen(
                viewModel = viewModel,
                onAddSuccess = { navController.popBackStack() }
            )
        }

        composable("remove_stock") {
            RemoveStockScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

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

    // Preview won't work without context, but this is just structure
    InventureApp(
        viewModel = fakeViewModel,
        authManager = AuthManager(androidx.compose.ui.platform.LocalContext.current),
        isDarkMode = false,
        onToggleDarkMode = {}
    )
}