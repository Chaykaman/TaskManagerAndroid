package com.example.taskmanager.data.local.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Таблица habits
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS habits (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                title TEXT NOT NULL,
                description TEXT NOT NULL,
                notificationEnabled INTEGER NOT NULL,
                frequency TEXT NOT NULL,
                daysOfWeek TEXT NOT NULL,
                createdAt TEXT NOT NULL,
                updatedAt TEXT NOT NULL
            )
        """)

        // Таблица habitLogs
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS habitLogs (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                habitId INTEGER NOT NULL,
                date TEXT NOT NULL,
                isCompleted INTEGER NOT NULL
            )
        """)

        // Уникальный индекс на habitLogs
        db.execSQL("""
            CREATE UNIQUE INDEX IF NOT EXISTS index_habitLogs_habitId_date
            ON habitLogs (habitId, date)
        """)
    }
}