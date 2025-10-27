package com.example.inventure


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface InventureDao {

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<Inventure>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Inventure)

    @Delete
    suspend fun deleteProduct(product: Inventure)

}
