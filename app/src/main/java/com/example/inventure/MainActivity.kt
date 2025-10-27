package com.example.inventure

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.example.inventure.ui.theme.InventureTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: InventureViewModel by viewModels {
        InventureViewModelFactory(
            (application as InventureApplication).repository
        )
    }

    private lateinit var themeManager: ThemeManager
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themeManager = ThemeManager(this)
        authManager = AuthManager(this)

        setContent {
            val isDarkMode by themeManager.isDarkMode.collectAsState(initial = false)
            val scope = rememberCoroutineScope()

            InventureTheme(darkTheme = isDarkMode) {
                InventureApp(
                    viewModel = viewModel,
                    authManager = authManager,
                    isDarkMode = isDarkMode,
                    onToggleDarkMode = { enabled ->
                        scope.launch {
                            themeManager.toggleDarkMode(enabled)
                        }
                    }
                )
            }
        }
    }
}