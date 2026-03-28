package com.example.taskmanager.feature.streak

import java.time.DayOfWeek
import java.time.LocalDate

object StreakCalculator {

    data class StreakResult(
        val currentStreak: Int,
        val maxStreak: Int,
        val maxStreakStartDate: LocalDate?,
        val maxStreakEndDate: LocalDate?
    )

    /**
     * Основная функция вычисления стриков.
     * @param completedPerDay Map<дата, количество выполненных задач>
     * @param minTasksPerDay Минимальный порог для засчёта дня
     * @param restDays Выходные дни, пропуск которых не ломает стрик
     * */
    fun calculate(
        completedPerDay: Map<LocalDate, Int>,
        minTasksPerDay: Int,
        restDays: Set<DayOfWeek>
    ): StreakResult {
        if (completedPerDay.isEmpty()) {
            return StreakResult(0, 0, null, null)
        }

        val today = LocalDate.now()

        // "Активные" дни - дни когда выполнен минимум задач
        val activeDays = completedPerDay
            .filter { (_, count) -> count >= minTasksPerDay }
            .keys
            .toSortedSet()

        // Строим все дни от самой ранней активной даты до сегодня
        // Для каждого дня определяем: засчитан ли он в стрик
        val firstDay = activeDays.firstOrNull() ?: return StreakResult(0, 0, null, null)
        val allDays = generateSequence(firstDay) { it.plusDays(1) }
            .takeWhile { !it.isAfter(today) }
            .toList()

        // Для каждого дня определяем засчитан ли он
        // День засчитан если: есть активность ИЛИ это выходной день
        // Выходной день не ломает стрик, но и не продвигает его
        fun isDayCounted(date: LocalDate): Boolean {
            return date in activeDays || date.dayOfWeek in restDays
        }

        // Ищем все серии подряд идущих засчитанных дней
        var currentSeriesStart: LocalDate? = null
        var currentSeriesLength = 0
        var maxSeriesLength = 0
        var maxSeriesStart: LocalDate? = null
        var maxSeriesEnd: LocalDate? = null

        allDays.forEach { date ->
            if (isDayCounted(date)) {
                if (currentSeriesStart == null) currentSeriesStart = date
                currentSeriesLength++

                if (currentSeriesLength > maxSeriesLength) {
                    maxSeriesLength = currentSeriesLength
                    maxSeriesStart = currentSeriesStart
                    maxSeriesEnd = date
                }
            } else {
                // Серия прервана
                currentSeriesStart = null
                currentSeriesLength = 0
            }
        }

        // Текущий стрик - это серия, которая включает сегодня или вчера
        // Если последний день серии это вчера или сегодня — стрик жив
        // Если нет - стрик сброшен
        val currentStreak = when {
            isDayCounted(today) -> currentSeriesLength
            // Проверяем вчера
            isDayCounted(today.minusDays(1)) -> {
                // Пересчитываем серию до вчера
                var length = 0
                var date = today.minusDays(1)
                while (!date.isBefore(firstDay) && isDayCounted(date)) {
                    length++
                    date = date.minusDays(1)
                }
                length
            }
            else -> 0
        }

        return StreakResult(
            currentStreak = currentStreak,
            maxStreak = maxSeriesLength,
            maxStreakStartDate = maxSeriesStart,
            maxStreakEndDate = maxSeriesEnd
        )
    }
}