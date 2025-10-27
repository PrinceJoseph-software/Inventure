package com.example.inventure

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Inventure::class], version = 1, exportSchema = false)
abstract class InventureDatabase : RoomDatabase() {

    abstract fun inventureDao(): InventureDao

    companion object {
        @Volatile
        private var INSTANCE: InventureDatabase? = null

        fun getDatabase(context: Context): InventureDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InventureDatabase::class.java,
                    "inventure_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
