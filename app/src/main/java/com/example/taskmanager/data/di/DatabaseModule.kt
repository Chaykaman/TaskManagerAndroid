package com.example.taskmanager.data.di

import android.content.Context
import com.example.taskmanager.data.local.database.MainDb
import com.example.taskmanager.data.local.dao.Dao
import com.example.taskmanager.data.local.dao.HabitDao
import com.example.taskmanager.data.local.dao.TaskDao
import com.example.taskmanager.data.local.dao.SurveyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MainDb {
        return MainDb.getDb(context)
    }

    @Provides
    fun provideDao(database: MainDb): Dao {
        return database.getDao()
    }

    @Provides
    fun provideTaskDao(database: MainDb): TaskDao {
        return database.getTaskDao()
    }

    @Provides
    fun provideSurveyDao(database: MainDb): SurveyDao {
        return database.getSurveyDao()
    }

    @Provides
    fun provideHabitDao(database: MainDb): HabitDao {
        return database.getHabitDao()
    }
}