package com.example.taskmanager.data.di

import com.example.taskmanager.data.repository.AppSettingsRepository
import com.example.taskmanager.data.repository.AppSettingsRepositoryImpl
import com.example.taskmanager.data.repository.DisplayOptionsRepository
import com.example.taskmanager.data.repository.DisplayOptionsRepositoryImpl
import com.example.taskmanager.data.repository.TaskRepository
import com.example.taskmanager.data.repository.TaskRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TaskRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindDisplayOptionsRepository(
        impl: DisplayOptionsRepositoryImpl
    ): DisplayOptionsRepository

    @Binds
    @Singleton
    abstract fun bindAppSettingsRepository(
        impl: AppSettingsRepositoryImpl
    ): AppSettingsRepository
}