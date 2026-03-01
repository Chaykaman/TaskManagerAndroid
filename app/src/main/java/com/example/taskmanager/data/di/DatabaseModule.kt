package com.example.taskmanager.di

import android.content.Context
import com.example.taskmanager.data.local.database.MainDb
import com.example.taskmanager.data.local.dao.Dao
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
    fun provideTaskDao(database: MainDb): Dao {
        return database.getDao()
    }
}