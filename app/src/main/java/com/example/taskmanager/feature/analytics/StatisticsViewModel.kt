package com.example.taskmanager.feature.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.local.dao.SurveyDao
import com.example.taskmanager.data.local.entity.Priority
import com.example.taskmanager.data.local.entity.Status
import com.example.taskmanager.data.repository.AnalyticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

data class WellbeingEntry(
    val date: String,
    val score: Float
)

data class StatisticsUiState(
    val isLoading: Boolean = true,
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val activeTasks: Int = 0,
    val completionRate: Float = 0f,
    val distributionByStatus: Map<Status, Int> = emptyMap(),
    val distributionByPriority: Map<Priority, Int> = emptyMap(),
    val productivityByDay: List<Pair<String, Int>> = emptyList(),
    val averageCompletionTime: Float? = null,
    val wellbeingByDay: List<WellbeingEntry> = emptyList(),
    val stressByDay: List<WellbeingEntry> = emptyList(),
    val productivityScoreByDay: List<WellbeingEntry> = emptyList(),
    val productivityTimeOfDay: Map<String, Int> = emptyMap(),
    val topFailureReasons: List<Pair<String, Int>> = emptyList(),
    // УДАЛЕНО: concentrationByDay
    val topDistractions: List<Pair<String, Int>> = emptyList(),
    val topTools: List<Pair<String, Int>> = emptyList(),
    val emotionDistribution: Map<String, Int> = emptyMap(),
    val topMotivations: List<Pair<String, Int>> = emptyList(),
    val hasSurveyData: Boolean = false
)

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val analyticsRepository: AnalyticsRepository,
    private val surveyDao: SurveyDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState

    init {
        loadStatistics()
    }

    fun loadStatistics() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val total = analyticsRepository.totalCount()
            val (active, completed) = analyticsRepository.completionStats()
            val rate = analyticsRepository.completionRate()
            val byStatus = analyticsRepository.distributionByStatus()
            val byPriority = analyticsRepository.distributionByPriority()
            val productivity = analyticsRepository.completedInPeriod(7)
            val avgTime = analyticsRepository.averageCompletionTime()

            val formatter = DateTimeFormatter.ofPattern("d MMM", Locale.forLanguageTag("ru"))
            val productivityFormatted = productivity.map { (date, count) ->
                date.format(formatter) to count
            }

            val today = LocalDate.now()
            val weekAgo = today.minusDays(6)

            val wellbeingScoreMap = mapOf(
                "Энергично" to 4f, "Нормально" to 3f,
                "Устал" to 2f, "Очень устал" to 1f
            )
            val wellbeingByDay = surveyDao.getResultsByQuestion(
                "Как вы себя чувствовали во время работы сегодня?", weekAgo, today
            ).map { WellbeingEntry(it.date.format(formatter), wellbeingScoreMap[it.answer] ?: 2f) }

            val stressScoreMap = mapOf(
                "Нет стресса" to 1f, "Лёгкий стресс" to 2f,
                "Умеренный" to 3f, "Сильный стресс" to 4f
            )
            val stressByDay = surveyDao.getResultsByQuestion(
                "Как бы вы оценили свой уровень стресса сегодня?", weekAgo, today
            ).map { WellbeingEntry(it.date.format(formatter), stressScoreMap[it.answer] ?: 2f) }

            val productivityScoreMap = mapOf(
                "Отлично" to 4f, "Хорошо" to 3f,
                "Удовлетворительно" to 2f, "Плохо" to 1f
            )
            val productivityScoreByDay = surveyDao.getResultsByQuestion(
                "Как вы оцениваете свою продуктивность сегодня?", weekAgo, today
            ).map { WellbeingEntry(it.date.format(formatter), productivityScoreMap[it.answer] ?: 2f) }

            val timeOfDayResults = surveyDao.getResultsByQuestion(
                "В какое время дня вы были наиболее продуктивны сегодня?", weekAgo, today
            )
            val productivityTimeOfDay = mapOf(
                "Утро" to timeOfDayResults.count { it.answer == "Утром" },
                "День" to timeOfDayResults.count { it.answer == "Днём" },
                "Вечер" to timeOfDayResults.count { it.answer == "Вечером" }
            )

            val topFailureReasons = surveyDao.getResultsByQuestion(
                "Что чаще всего мешало вам выполнять задачи сегодня?", weekAgo, today
            ).filter { it.answer != "Ничего не мешало" }
                .groupBy { it.answer }
                .map { (answer, list) -> answer to list.size }
                .sortedByDescending { it.second }
                .take(5)

            val topDistractions = surveyDao.getResultsByQuestion(
                "Что чаще всего отвлекало вас сегодня?", weekAgo, today
            ).groupBy { it.answer }
                .map { (answer, list) -> answer to list.size }
                .sortedByDescending { it.second }
                .take(5)

            val topTools = surveyDao.getResultsByQuestion(
                "Какие инструменты вы использовали для выполнения задач сегодня?", weekAgo, today
            ).filter { it.answer != "Не использовал доп. инструменты" }
                .groupBy { it.answer }
                .map { (answer, list) -> answer to list.size }
                .sortedByDescending { it.second }
                .take(5)

            val emotionDistribution = surveyDao.getResultsByQuestion(
                "Какие эмоции у вас остались после рабочего дня?", weekAgo, today
            ).groupBy { it.answer }
                .map { (answer, list) -> answer to list.size }
                .sortedByDescending { it.second }
                .toMap()

            val topMotivations = surveyDao.getResultsByQuestion(
                "Что больше всего мотивировало вас выполнять задачи сегодня?", weekAgo, today
            ).filter { it.answer != "Ничего не мотивировало" }
                .groupBy { it.answer }
                .map { (answer, list) -> answer to list.size }
                .sortedByDescending { it.second }
                .take(5)

            val hasSurveyData = surveyDao.getResultsInPeriod(weekAgo, today).isNotEmpty()

            _uiState.value = StatisticsUiState(
                isLoading = false,
                totalTasks = total,
                completedTasks = completed,
                activeTasks = active,
                completionRate = rate,
                distributionByStatus = byStatus,
                distributionByPriority = byPriority,
                productivityByDay = productivityFormatted,
                averageCompletionTime = avgTime,
                wellbeingByDay = wellbeingByDay,
                stressByDay = stressByDay,
                productivityScoreByDay = productivityScoreByDay,
                productivityTimeOfDay = productivityTimeOfDay,
                topFailureReasons = topFailureReasons,
                topDistractions = topDistractions,
                topTools = topTools,
                emotionDistribution = emotionDistribution,
                topMotivations = topMotivations,
                hasSurveyData = hasSurveyData
            )
        }
    }
}