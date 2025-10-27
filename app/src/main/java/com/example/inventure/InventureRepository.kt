package com.example.inventure

import kotlinx.coroutines.flow.Flow

class ProductRepository(private val dao: InventureDao) {

    val allProducts = dao.getAllProducts()

    suspend fun addProduct(product: Inventure) {
        dao.insertProduct(product)
    }

    suspend fun deleteProduct(product: Inventure) {
        dao.deleteProduct(product)
    }
}
