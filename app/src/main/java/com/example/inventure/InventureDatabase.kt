package com.example.inventure

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Inventure::class], version = 2, exportSchema = false)
abstract class InventureDatabase : RoomDatabase() {

    abstract fun inventureDao(): InventureDao

    companion object {
        @Volatile
        private var INSTANCE: InventureDatabase? = null

        //Migration to add imageUri column
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE products ADD COLUMN imageUri TEXT")
            }
        }

        fun getDatabase(context: Context): InventureDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InventureDatabase::class.java,
                    "inventure_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}