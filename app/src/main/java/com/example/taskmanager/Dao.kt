package com.example.taskmanager

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Insert
    suspend fun insertTask(tasks: Tasks)

    @Query("SELECT * FROM Tasks")
    fun getAllItem(): Flow<List<Tasks>>
}