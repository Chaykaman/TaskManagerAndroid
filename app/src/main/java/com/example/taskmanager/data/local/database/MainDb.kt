package com.example.taskmanager.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.taskmanager.data.local.dao.Dao
import com.example.taskmanager.data.local.entity.Task

@Database(
    entities = [Task::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MainDb : RoomDatabase() {

    abstract fun getDao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: MainDb? = null

        fun getDb(context: Context): MainDb {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MainDb::class.java,
                    "task.db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}