package com.example.inventure

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "inventures")
data class Inventure(
       @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val name: String,
        val description: String,
        val quantity: Int = 0,
        val price: Double = 0.0,
        val category: String,
        val supplier: String,
        val dateAdded: Long = System.currentTimeMillis()
    )


