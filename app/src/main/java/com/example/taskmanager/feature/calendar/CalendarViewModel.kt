package com.example.taskmanager.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.local.entity.Task
import com.example.taskmanager.data.logger.TaskLogger
import com.example.taskmanager.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    // Состояние UI
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        observeCalendarData()
        TaskLogger.i("[CalendarViewModel] Инициализирован")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeCalendarData() {
        viewModelScope.launch {
            combine(
                _uiState.map { it.selectedDate }.distinctUntilChanged(),
                _uiState.map { it.displayedMonth }.distinctUntilChanged(),
                _uiState.map { it.displayedWeekStart  }.distinctUntilChanged(),
                _uiState.map { it.viewMode }.distinctUntilChanged()
            ) { selectedDate, displayedMonth, displayedWeekStart, viewMode ->
                CalendarDataParams(selectedDate, displayedMonth, displayedWeekStart , viewMode)
            }.flatMapLatest { params ->
                val (rangeStart, rangeEnd) = when (params.viewMode) {
                    CalendarViewMode.WEEK -> {
                        params.displayedWeekStart to params.displayedWeekStart.plusDays(6)
                    }
                    CalendarViewMode.MONTH -> {
                        params.displayedMonth.atDay(1) to params.displayedMonth.atEndOfMonth()
                    }
                }
                taskRepository.getTasksForDate(params.selectedDate)
                    .combine(
                        taskRepository.getTasksForDateRange(rangeStart, rangeEnd)
                    ) { tasksForDay, tasksForRange ->

                        // Номера дней месяца с задачами
                        val daysWithTasks = tasksForRange
                            .mapNotNull { it.dueDate }
                            .toSet()

                        _uiState.update { currentState ->
                            currentState.copy(
                                tasksForSelectedDate = tasksForDay,
                                daysWithTasks = daysWithTasks,
                                isLoading = false
                            )
                        }
                    }
            }.collect()
        }
    }

    /**
     * Обновление отображаемой даты
     * @param date Дата
     */
    fun onDateSelected(date: LocalDate) {
        _uiState.update {
            it.copy(
                selectedDate = date,
                displayedMonth = YearMonth.from(date),
                displayedWeekStart = date.with(DayOfWeek.MONDAY)
            )
        }
    }

    /**
     * Обновление отображаемого месяца
     * @param yearMonth Месяц
     */
    fun onMonthChanged(yearMonth: YearMonth) {
        _uiState.update {
            it.copy(
                displayedMonth = yearMonth,
                displayedWeekStart = yearMonth.atDay(1).with(DayOfWeek.MONDAY)
            )
        }
    }

    /**
     * Обновление отображаемой недели
     * @param weekStart День начала недели
     */
    fun onWeekChanged(weekStart: LocalDate) {
        _uiState.update {
            it.copy(
                displayedWeekStart = weekStart,
                displayedMonth = YearMonth.from(weekStart.plusDays(3))
            )
        }
    }

    /**
     * Обновление режима отображения
     * @param mode Режим отображения (WEEK, MONTH)
     */
    fun onViewModeChanged(mode: CalendarViewMode) {
        _uiState.update {
            it.copy(
                viewMode = mode,
                displayedMonth = YearMonth.from(it.selectedDate),
                displayedWeekStart = it.selectedDate.with(DayOfWeek.MONDAY)
            )
        }
    }

    /**
     * Обновление статуса выполнения задачи
     * @param task Обновлённая задача
     */
    suspend fun toggleTaskCompletion(task: Task) {
        taskRepository.updateTask(
            task = task.copy(isCompleted = !task.isCompleted)
        )
    }

    /**
     * Удаление задачи
     * @param taskId Идентификатор задачи
     */
    suspend fun deleteTask(taskId: Int) {
        taskRepository.deleteTask(id = taskId)
    }

}