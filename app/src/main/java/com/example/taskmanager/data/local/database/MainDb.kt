package com.example.taskmanager.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.taskmanager.data.local.dao.AchievementDao
import com.example.taskmanager.data.local.dao.Dao
import com.example.taskmanager.data.local.dao.HabitDao
import com.example.taskmanager.data.local.dao.SurveyDao
import com.example.taskmanager.data.local.dao.TaskDao
import com.example.taskmanager.data.local.entity.habit.Habit
import com.example.taskmanager.data.local.entity.habit.HabitLog
import com.example.taskmanager.data.local.entity.SurveyResult
import com.example.taskmanager.data.local.entity.Task
import com.example.taskmanager.data.local.entity.achievement.AchievementProgress
import com.example.taskmanager.data.local.database.migrations.MIGRATION_1_2
import com.example.taskmanager.data.local.database.migrations.MIGRATION_2_3
import com.example.taskmanager.data.local.database.migrations.MIGRATION_3_4
import com.example.taskmanager.data.local.database.migrations.MIGRATION_4_5
import com.example.taskmanager.data.local.database.migrations.MIGRATION_5_6

@Database(
    entities = [
        Task::class,
        SurveyResult::class,
        Habit::class,
        HabitLog::class,
        AchievementProgress::class
    ],
    version = 6,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MainDb : RoomDatabase() {

    abstract fun getDao(): Dao
    abstract fun getTaskDao(): TaskDao
    abstract fun getSurveyDao(): SurveyDao
    abstract fun getHabitDao(): HabitDao
    abstract fun achievementDao(): AchievementDao

    companion object {
        @Volatile
        private var INSTANCE: MainDb? = null

        fun getDb(context: Context): MainDb {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MainDb::class.java,
                    "task.db"
                )
                    .addMigrations(
                        MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6
                    )
                    .build().also { INSTANCE = it }
            }
        }
    }
}
