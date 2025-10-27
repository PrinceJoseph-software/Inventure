package com.example.inventure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

class InventureViewModel(private val repository: ProductRepository) : ViewModel() {

    // Expose the Flow directly from repository
    val products = repository.allProducts

    fun addProduct(
        name: String,
        description: String,
        price: Double,
        quantity: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val product = Inventure(
                name = name,
                description = description,
                price = price,
                quantity = quantity
            )
            repository.addProduct(product)
        }
    }

    fun deleteProduct(product: Inventure) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteProduct(product)
        }
    }
}