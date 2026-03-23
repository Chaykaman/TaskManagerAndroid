package com.example.taskmanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "survey_results")
data class SurveyResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: LocalDate = LocalDate.now(),
    val category: String,
    val question: String,
    val answer: String
)

