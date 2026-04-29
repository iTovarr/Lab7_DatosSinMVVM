package com.example.datossinmvvm

import androidx.room.*

@Dao
interface TaskDao {
    @Query("SELECT * FROM Task")
    suspend fun getAllTasks(): List<Task>

    @Insert
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    // Consultas para los pasos
    @Query("SELECT * FROM TaskStep WHERE taskId = :taskId")
    suspend fun getStepsForTask(taskId: Int): List<TaskStep>

    @Insert
    suspend fun insertStep(step: TaskStep)
}