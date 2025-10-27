package com.example.inventure

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

class MainActivity : ComponentActivity() {

    private val viewModel: InventureViewModel by viewModels {
        InventureViewModelFactory(
            (application as InventureApplication).repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InventureApp(viewModel = viewModel)
        }
    }
}
