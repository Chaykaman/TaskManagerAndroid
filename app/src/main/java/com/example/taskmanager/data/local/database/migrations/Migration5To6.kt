package com.example.taskmanager.data.local.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `achievement_progress` (
                achievementId TEXT NOT NULL, 
                currentProgress INTEGER NOT NULL, 
                isUnlocked INTEGER NOT NULL, 
                unlockedAt TEXT, 
                PRIMARY KEY(`achievementId`)
            )
        """)
    }
}