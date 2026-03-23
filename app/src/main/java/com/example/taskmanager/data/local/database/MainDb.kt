package com.example.taskmanager.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.taskmanager.data.local.dao.Dao
import com.example.taskmanager.data.local.dao.SurveyDao
import com.example.taskmanager.data.local.dao.TaskDao
import com.example.taskmanager.data.local.entity.SurveyResult
import com.example.taskmanager.data.local.entity.Task

@Database(
    entities = [Task::class, SurveyResult::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MainDb : RoomDatabase() {

    abstract fun getDao(): Dao

    abstract fun getTaskDao(): TaskDao
    abstract fun getSurveyDao(): SurveyDao

    companion object {

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS survey_results (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        date TEXT NOT NULL,
                        category TEXT NOT NULL,
                        question TEXT NOT NULL,
                        answer TEXT NOT NULL
                    )
                """)
            }
        }
        @Volatile
        private var INSTANCE: MainDb? = null

        fun getDb(context: Context): MainDb {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MainDb::class.java,
                    "task.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build().also {
                    INSTANCE = it
                }
            }
        }
    }
}