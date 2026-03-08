package com.example.taskmanager.feature.calendar

enum class CalendarViewMode {
    WEEK,
    MONTH;

    fun switch(): CalendarViewMode = when (this) {
        WEEK -> MONTH
        MONTH -> WEEK
    }
}