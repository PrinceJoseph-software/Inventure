package com.example.inventure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.analytics.ecommerce.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InventureViewModel(private val repository: InventureRepository) : ViewModel() {

    // StateFlow to hold the list of all products
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    // Initialize by loading all products
    init {
        loadProducts()
    }

    // Function to load all products from the database
    private fun loadProducts() {
        viewModelScope.launch {
            repository.getAllProducts().collect { productList ->
                _products.value = productList
            }
        }
    }

    // Function to add a new product
    fun addProduct(
        image: String,
        name: String,
        description: String,
        price: Double,
        quantity: Int
    ) {
        viewModelScope.launch {
            val product = Product(
                image = image,
                name = name,
                description = description,
                price = price,
                quantity = quantity
            )
            repository.insertProduct(product)
        }
    }

    // Function to delete a product
    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }

    // Function to update a product
    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }
}