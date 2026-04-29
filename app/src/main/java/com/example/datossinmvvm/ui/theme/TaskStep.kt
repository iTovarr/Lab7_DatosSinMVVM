package com.example.datossinmvvm

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TaskStep(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val taskId: Int,
    val stepDescription: String
)