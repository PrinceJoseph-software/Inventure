package com.example.inventure

import kotlinx.coroutines.flow.Flow

class ProductRepository(private val dao: InventureDao) {

    val allProducts = dao.getAllProducts() // rename 'products' to 'allProducts'

    suspend fun addProduct(product: Inventure) { // rename method
        dao.insertProduct(product)
    }

    suspend fun deleteProduct(product: Inventure) {
        dao.deleteProduct(product)
    }
}
