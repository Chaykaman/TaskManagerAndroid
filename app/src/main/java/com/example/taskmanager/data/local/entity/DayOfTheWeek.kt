package com.example.taskmanager.data.local.entity

enum class DayOfTheWeek (
    val id: Int,
    val label: String,
)
{
    MONDAY(
        id = 1,
        label = "Понедельник",
    ),
    TUESDAY(
        id = 2,
        label = "Вторник",
    ),
    WEDNESDAY(
        id = 3,
        label = "Среда",
    ),
    THURSDAY(
        id = 4,
        label = "Четверг",),
    FRIDAY(
        id = 5,
        label = "Пятница",
    ),
    SATURDAY(
        id = 6,
        label = "Суббота",
    ),
    SUNDAY(
        id = 7,
        label = "Воскресенье",
    )

}