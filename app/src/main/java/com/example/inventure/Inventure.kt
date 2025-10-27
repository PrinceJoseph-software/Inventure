package com.example.inventure

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Inventure(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val quantity: Int,
    val imageUri: String? = null  //image field
)