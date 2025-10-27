package com.example.inventure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers


class InventureViewModel(private val repository: ProductRepository) : ViewModel() {

    // Expose the Flow directly from repository
    val products = repository.allProducts


    //Dashboard
    val totalProducts: StateFlow<Int> = products.map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalValue: StateFlow<Double> = products.map { productList ->
        productList.sumOf { it.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val lowStockCount: StateFlow<Int> = products.map { productList ->
        productList.count { it.quantity < 10 }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun addProduct(
        name: String,
        description: String,
        price: Double,
        quantity: Int,
        imageUri: String? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val product = Inventure(
                name = name,
                description = description,
                price = price,
                quantity = quantity,
                imageUri = imageUri
            )
            repository.addProduct(product)
        }
    }

    fun deleteProduct(product: Inventure) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteProduct(product)
        }
    }
    fun updateProductQuantity(product: Inventure, quantityToRemove: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val newQuantity = product.quantity - quantityToRemove
            if (newQuantity > 0) {
                val updatedProduct = product.copy(quantity = newQuantity)
                repository.addProduct(updatedProduct) // Update the product quantity
            } else {
                repository.deleteProduct(product) // Remove if quantity reaches 0
            }
        }
    }
}