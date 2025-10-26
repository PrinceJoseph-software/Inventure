package com.example.inventure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.analytics.ecommerce.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InventureViewModel(private val repository: InventureRepository) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            repository.getAllProducts().collect { productList ->
                _products.value = productList
            }
        }
    }

    fun addProduct(
        image: String,
        name: String,
        price: Double,
        description: String,
        quantity: Int
    ) {
        viewModelScope.launch {
            val product = Product(
                image = image,
                name = name,
                price = price,
                description = description,
                quantity = quantity
            )
            repository.insertProduct(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }
}