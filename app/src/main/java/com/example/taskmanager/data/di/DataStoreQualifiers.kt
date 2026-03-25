package com.example.taskmanager.data.di

import jakarta.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DisplayOptionsDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppSettingsDataStore