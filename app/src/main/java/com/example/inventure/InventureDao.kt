package com.example.inventure

// InventureDao.kt (the DAO interface)
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

    // Add more queries if needed, for example:
    // @Query("SELECT * FROM products WHERE name LIKE :search")
    // fun searchProducts(search: String): Flow<List<Inventure>>
}
