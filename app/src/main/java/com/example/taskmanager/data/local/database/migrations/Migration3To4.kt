package com.example.taskmanager.data.local.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL("DROP TABLE IF EXISTS habitLogs")
        db.execSQL("DROP TABLE IF EXISTS habits")

        db.execSQL("""
            CREATE TABLE habits (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                title TEXT NOT NULL,
                description TEXT NOT NULL,
                iconName TEXT NOT NULL,
                color INTEGER NOT NULL,
                frequency TEXT NOT NULL,
                daysOfWeek TEXT NOT NULL,
                notificationEnabled INTEGER NOT NULL,
                isArchived INTEGER NOT NULL,
                createdAt TEXT NOT NULL,
                updatedAt TEXT NOT NULL
            )
        """)

        db.execSQL("""
            CREATE TABLE habit_logs (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                habitId INTEGER NOT NULL,
                date TEXT NOT NULL,
                isCompleted INTEGER NOT NULL DEFAULT 1,
                completedAt TEXT
            )
        """)

        db.execSQL("""
            CREATE UNIQUE INDEX index_habit_logs_habitId_date 
            ON habit_logs (habitId, date)
        """)
    }
}