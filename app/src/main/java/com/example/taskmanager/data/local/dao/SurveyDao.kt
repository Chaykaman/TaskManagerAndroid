package com.example.taskmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.taskmanager.data.local.entity.SurveyResult
import java.time.LocalDate

@Dao
interface SurveyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(results: List<SurveyResult>)

    // Все ответы за период
    @Query("SELECT * FROM survey_results WHERE date BETWEEN :from AND :to ORDER BY date ASC")
    suspend fun getResultsInPeriod(from: LocalDate, to: LocalDate): List<SurveyResult>

    // Ответы по категории за период
    @Query("""
        SELECT * FROM survey_results 
        WHERE category = :category 
        AND date BETWEEN :from AND :to 
        ORDER BY date ASC
    """)
    suspend fun getResultsByCategory(
        category: String,
        from: LocalDate,
        to: LocalDate
    ): List<SurveyResult>

    // Все ответы на конкретный вопрос за период
    @Query("""
        SELECT * FROM survey_results 
        WHERE question = :question 
        AND date BETWEEN :from AND :to
    """)
    suspend fun getResultsByQuestion(
        question: String,
        from: LocalDate,
        to: LocalDate
    ): List<SurveyResult>

    // Проверка — проходил ли пользователь опрос сегодня
    @Query("SELECT COUNT(*) FROM survey_results WHERE date = :date")
    suspend fun countForDate(date: LocalDate): Int

    // Удаление всех ответов за конкретный день
    @Query("DELETE FROM survey_results WHERE date = :date")
    suspend fun deleteForDate(date: LocalDate)
}

