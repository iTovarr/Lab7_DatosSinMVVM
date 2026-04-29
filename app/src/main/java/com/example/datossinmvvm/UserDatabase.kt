package com.example.datossinmvvm

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class, Task::class, TaskStep::class], version = 5)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun taskDao(): TaskDao
}