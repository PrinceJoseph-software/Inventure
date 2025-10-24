package com.example.inventure

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


interface InventureDao {
    @Query(value = "Select * From inventures ORDER BY dateAdded DESC ")
    fun getAllInventures() : Flow<List<Inventure>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventure(inventure: Inventure)

    @Delete
    suspend fun deleteInventure(inventure: Inventure)

    @Query(value = "DELETE from inventures  ")
    suspend fun deleteAllInventures(inventure: Inventure)


}