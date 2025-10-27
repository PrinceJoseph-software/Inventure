package com.example.inventure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class InventureViewModelFactory(private val repository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventureViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventureViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
