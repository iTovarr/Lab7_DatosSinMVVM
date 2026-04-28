package com.example.datossinmvvm

import androidx.room.*
import com.example.datossinmvvm.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM Product")
    suspend fun getAll(): List<Product>

    @Insert
    suspend fun insert(product: Product)

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("DELETE FROM Product")
    suspend fun deleteAll()
}