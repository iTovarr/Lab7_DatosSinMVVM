package com.example.datossinmvvm

import androidx.room.*

@Dao
interface TaskDao {
    @Query("SELECT * FROM Task")
    suspend fun getAllTasks(): List<Task>

    @Insert
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}