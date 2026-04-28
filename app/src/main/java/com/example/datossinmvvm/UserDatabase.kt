package com.example.datossinmvvm

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class, Product::class], version = 2) // Sube la versión a 2
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao // Añade el DAO de productos
}
