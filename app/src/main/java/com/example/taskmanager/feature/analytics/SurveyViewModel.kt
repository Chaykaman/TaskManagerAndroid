package com.example.taskmanager.feature.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.local.dao.Dao
import com.example.taskmanager.data.local.dao.SurveyDao
import com.example.taskmanager.data.local.entity.Priority
import com.example.taskmanager.data.local.entity.SurveyResult
import com.example.taskmanager.data.local.entity.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class SurveyQuestion(
    val text: String,
    val options: List<String>,
    val category: SurveyCategory,
    // НОВОЕ: id вопроса для ветвления
    val id: String = "",
    // НОВОЕ: показывать только если на вопрос triggerQuestionId дан ответ из triggerAnswers
    val triggerQuestionId: String? = null,
    val triggerAnswers: List<String> = emptyList()
)

enum class SurveyCategory(val label: String) {
    PRODUCTIVITY("Продуктивность"),
    TASK_COMPLETION("Выполнение задач"),
    PLANNING("Планирование"),
    WELLBEING("Самочувствие и энергия"),
    PRIORITY("Приоритеты"),
    TOOLS("Инструменты и AI"),
    OVERDUE("Просроченные задачи"),
    MOTIVATION("Мотивация и эмоции"),
    CONCENTRATION("Концентрация"),
    EXTERNAL("Внешние факторы")
}

data class SurveyUiState(
    val questions: List<SurveyQuestion> = emptyList(),
    val answers: Map<Int, String> = emptyMap(),
    // НОВОЕ: ответы по id вопроса для ветвления
    val answerById: Map<String, String> = emptyMap(),
    val currentIndex: Int = 0,
    val isFinished: Boolean = false,
    val isLoading: Boolean = true,
    val alreadyCompleted: Boolean = false
)

@HiltViewModel
class SurveyViewModel @Inject constructor(
    private val dao: Dao,
    private val surveyDao: SurveyDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(SurveyUiState())
    val uiState: StateFlow<SurveyUiState> = _uiState

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            val today = LocalDate.now()
            val alreadyCompleted = surveyDao.countForDate(today) > 0

            if (alreadyCompleted) {
                _uiState.value = SurveyUiState(isLoading = false, alreadyCompleted = true)
                return@launch
            }

            var tasks = dao.getForToday(today).first()
            if (tasks.isEmpty()) tasks = dao.getActive().first()

            val questions = buildQuestions(tasks)
            _uiState.value = SurveyUiState(questions = questions, isLoading = false)
        }
    }

    // ИЗМЕНЕНО: answerQuestion теперь поддерживает ветвление
    fun answerQuestion(answer: String) {
        val state = _uiState.value
        val currentQuestion = state.questions[state.currentIndex]

        val newAnswers = state.answers.toMutableMap()
        newAnswers[state.currentIndex] = answer

        // Сохраняем ответ по id для ветвления
        val newAnswerById = state.answerById.toMutableMap()
        if (currentQuestion.id.isNotEmpty()) {
            newAnswerById[currentQuestion.id] = answer
        }

        // Ищем следующий вопрос с учётом ветвления
        var nextIndex = state.currentIndex + 1
        while (nextIndex < state.questions.size) {
            val nextQuestion = state.questions[nextIndex]
            // Если вопрос условный — проверяем триггер
            if (nextQuestion.triggerQuestionId != null) {
                val triggerAnswer = newAnswerById[nextQuestion.triggerQuestionId]
                if (triggerAnswer != null && triggerAnswer in nextQuestion.triggerAnswers) {
                    break // показываем этот вопрос
                } else {
                    nextIndex++ // пропускаем
                }
            } else {
                break // обычный вопрос — показываем
            }
        }

        val isFinished = nextIndex >= state.questions.size

        _uiState.value = state.copy(
            answers = newAnswers,
            answerById = newAnswerById,
            currentIndex = if (isFinished) state.currentIndex else nextIndex,
            isFinished = isFinished
        )

        if (isFinished) {
            saveResults(state.questions, newAnswers)
        }
    }

    private fun saveResults(questions: List<SurveyQuestion>, answers: Map<Int, String>) {
        viewModelScope.launch {
            val today = LocalDate.now()
            val results = answers.map { (index, answer) ->
                SurveyResult(
                    date = today,
                    category = questions[index].category.name,
                    question = questions[index].text,
                    answer = answer
                )
            }
            surveyDao.insertAll(results)
        }
    }

    // ИЗМЕНЕНО: buildQuestions — новая логика построения опроса
    private fun buildQuestions(tasks: List<Task>): List<SurveyQuestion> {
        val totalCount = tasks.size
        val completedCount = tasks.count { it.isCompleted }
        val overdueCount = tasks.count { it.isOverdue() }
        val highPriorityCount = tasks.count {
            it.priority == Priority.PRIORITY_1 || it.priority == Priority.PRIORITY_2
        }

        val selected = mutableListOf<SurveyQuestion>()

        // ===== ОБЯЗАТЕЛЬНЫЕ ВОПРОСЫ (всегда присутствуют) =====

        // PRODUCTIVITY — обязательные
        selected.add(SurveyQuestion(
            id = "productivity_score",
            text = "Как вы оцениваете свою продуктивность сегодня?",
            options = listOf("Отлично", "Хорошо", "Удовлетворительно", "Плохо"),
            category = SurveyCategory.PRODUCTIVITY
        ))
        selected.add(SurveyQuestion(
            id = "productivity_time",
            text = "В какое время дня вы были наиболее продуктивны сегодня?",
            options = listOf("Утром", "Днем", "Вечером"),
            category = SurveyCategory.PRODUCTIVITY
        ))

        // WELLBEING — обязательные
        selected.add(SurveyQuestion(
            id = "wellbeing_feel",
            text = "Как вы себя чувствовали во время работы сегодня?",
            options = listOf("Энергично", "Нормально", "Устал", "Очень устал"),
            category = SurveyCategory.WELLBEING
        ))
        selected.add(SurveyQuestion(
            id = "wellbeing_stress",
            text = "Как бы вы оценили свой уровень стресса сегодня?",
            options = listOf("Нет стресса", "Лёгкий стресс", "Умеренный", "Сильный стресс"),
            category = SurveyCategory.WELLBEING
        ))

        // TASK_COMPLETION — обязательный
        selected.add(SurveyQuestion(
            id = "task_obstacles",
            text = "Что чаще всего мешало вам выполнять задачи сегодня?",
            options = listOf("Отвлекающие факторы", "Усталость", "Нехватка мотивации", "Плохое планирование", "Ничего не мешало"),
            category = SurveyCategory.TASK_COMPLETION
        ))

        // CONCENTRATION — обязательный + ветвление
        selected.add(SurveyQuestion(
            id = "concentration_freq",
            text = "Часто ли вы отвлекались во время работы сегодня?",
            options = listOf("Совсем нет", "Редко", "Иногда", "Часто", "Постоянно"),
            category = SurveyCategory.CONCENTRATION
        ))
        // условный вопрос — показывается только если отвлекался
        selected.add(SurveyQuestion(
            id = "concentration_what",
            text = "Что чаще всего отвлекало вас сегодня?",
            options = listOf("Уведомления и телефон", "Другие задачи", "Люди вокруг", "Усталость", "Соцсети"),
            category = SurveyCategory.CONCENTRATION,
            triggerQuestionId = "concentration_freq",
            triggerAnswers = listOf("Редко", "Иногда", "Часто", "Постоянно")
        ))
        // нейтральный вопрос чтобы не было видимого перескока после концентрации
        selected.add(SurveyQuestion(
            id = "planning_match",
            text = "Соответствовал ли ваш план дня реальности?",
            options = listOf("Полностью", "В основном да", "Частично", "Совсем нет", "Не планировал"),
            category = SurveyCategory.PLANNING
        ))

        // TOOLS — обязательный
        selected.add(SurveyQuestion(
            id = "tools_used",
            text = "Какие инструменты вы использовали для выполнения задач сегодня?",
            options = listOf("Искусственный интеллект", "Поиск в интернете", "Специализированные приложения", "Помощь других людей", "Не использовал доп. инструменты"),
            category = SurveyCategory.TOOLS
        ))

        // MOTIVATION — обязательные
        selected.add(SurveyQuestion(
            id = "motivation_emotion",
            text = "Какие эмоции у вас остались после рабочего дня?",
            options = listOf("Удовлетворение", "Облегчение", "Усталость", "Безразличие", "Раздражение"),
            category = SurveyCategory.MOTIVATION
        ))
        selected.add(SurveyQuestion(
            id = "motivation_source",
            text = "Что больше всего мотивировало вас выполнять задачи сегодня?",
            options = listOf("Личный интерес", "Сроки / Дедлайн", "Ответственность перед другими", "Польза результата", "Ничего не мотивировало"),
            category = SurveyCategory.MOTIVATION
        ))

        // OVERDUE — условный (только если есть просроченные)
        if (overdueCount > 0) {
            selected.add(SurveyQuestion(
                id = "overdue_reason",
                text = "У вас $overdueCount просроченных задач. Почему не успели их выполнить?",
                options = listOf("Не хватило времени", "Задачи оказались сложнее", "Потерял интерес / мотивацию", "Были более важные задачи", "Внешние обстоятельства"),
                category = SurveyCategory.OVERDUE
            ))
        }

        // ===== ДОПОЛНИТЕЛЬНЫЕ ВОПРОСЫ (добираем до 15) =====
        val extraPool = buildExtraPool(tasks, totalCount, completedCount, highPriorityCount)
        val usedTexts = selected.map { it.text }.toSet()

        for (question in extraPool.shuffled()) {
            if (selected.size >= 15) break
            if (question.text !in usedTexts) {
                selected.add(question)
            }
        }

        return selected
    }

    // пул дополнительных вопросов
    private fun buildExtraPool(
        tasks: List<Task>,
        totalCount: Int,
        completedCount: Int,
        highPriorityCount: Int
    ): List<SurveyQuestion> {
        return listOf(
            // PRODUCTIVITY доп.
            SurveyQuestion(
                text = "Насколько вы довольны своей продуктивностью за последнее время?",
                options = listOf("Совсем не доволен", "Скорее не доволен", "Нормально", "Доволен", "Очень доволен"),
                category = SurveyCategory.PRODUCTIVITY
            ),
            SurveyQuestion(
                text = "Делали ли вы перерывы в течение дня?",
                options = listOf("Да, регулярно", "Иногда", "Редко", "Совсем нет"),
                category = SurveyCategory.PRODUCTIVITY
            ),
            SurveyQuestion(
                text = "В какие дни вы чувствуете себя наиболее продуктивно?",
                options = listOf("Будние дни", "Выходные", "Одинаково", "Затрудняюсь ответить"),
                category = SurveyCategory.PRODUCTIVITY
            ),
            // WELLBEING доп.
            SurveyQuestion(
                text = "Удалось ли вам соблюдать баланс работы и отдыха?",
                options = listOf("Да", "Скорее да", "Скорее нет", "Нет"),
                category = SurveyCategory.WELLBEING
            ),
            SurveyQuestion(
                text = "Как вы себя чувствуете в конце дня?",
                options = listOf("Бодро и доволен", "Нормально", "Немного устал", "Очень устал"),
                category = SurveyCategory.WELLBEING
            ),
            // TASK_COMPLETION доп.
            SurveyQuestion(
                text = "Довольны ли вы результатами работы сегодня?",
                options = listOf("Да, доволен", "Мог бы лучше", "Нет, не доволен", "Всё равно"),
                category = SurveyCategory.TASK_COMPLETION
            ),
            SurveyQuestion(
                text = "Как вы оцениваете количество задач на сегодня?",
                options = listOf("В самый раз", "Слишком много", "Слишком мало", "Не обращал внимания"),
                category = SurveyCategory.TASK_COMPLETION
            ),
            SurveyQuestion(
                text = "Насколько сложными были задачи сегодня?",
                options = listOf("Очень сложные", "Сложные", "Средние", "Простые", "Очень простые"),
                category = SurveyCategory.TASK_COMPLETION
            ),
            SurveyQuestion(
                text = "Хватало ли вам ресурсов для выполнения задач сегодня?",
                options = listOf("Да, всего хватало", "Не хватало времени", "Не хватало инструментов", "Не хватало знаний"),
                category = SurveyCategory.TASK_COMPLETION
            ),
            // PLANNING доп.
            SurveyQuestion(
                text = "Соответствовал ли ваш план дня реальности?",
                options = listOf("Полностью", "В основном да", "Частично", "Совсем нет"),
                category = SurveyCategory.PLANNING
            ),
            SurveyQuestion(
                text = "Вы заранее планировали задачи на сегодня?",
                options = listOf("Да, вчера", "Да, утром", "По ходу дня", "Нет"),
                category = SurveyCategory.PLANNING
            ),
            SurveyQuestion(
                text = "Появлялись ли в течение дня незапланированные задачи?",
                options = listOf("Нет", "Одна-две", "Несколько", "Очень много"),
                category = SurveyCategory.PLANNING
            ),
            SurveyQuestion(
                text = "Готовы ли вы к задачам на завтра?",
                options = listOf("Да, всё спланировано", "Буду планировать", "Ещё не думал", "Нет"),
                category = SurveyCategory.PLANNING
            ),
            // PRIORITY доп.
            SurveyQuestion(
                text = "Вы выполняли задачи в порядке их приоритета?",
                options = listOf("Да, всегда", "Иногда", "Нет", "Не думал об этом"),
                category = SurveyCategory.PRIORITY
            ),
            SurveyQuestion(
                text = "Все ли важные задачи на сегодня были выполнены?",
                options = listOf("Да, все", "Большинство", "Только некоторые", "Ни одной", "Их не было"),
                category = SurveyCategory.PRIORITY
            ),
            // TOOLS доп.
            SurveyQuestion(
                text = "Помогло ли приложение лучше организовать ваш день?",
                options = listOf("Очень помогло", "Немного помогло", "Нейтрально", "Не помогло"),
                category = SurveyCategory.TOOLS
            ),
            SurveyQuestion(
                text = "Пользовались ли вы уведомлениями о задачах сегодня?",
                options = listOf("Да, очень полезно", "Иногда", "Редко", "Не использовал"),
                category = SurveyCategory.TOOLS
            ),
            // MOTIVATION доп.
            SurveyQuestion(
                text = "Что помогло вам выполнить задачи сегодня?",
                options = listOf("Чёткий план", "Напоминания", "Помощь других людей", "Использование технологий / ИИ", "Самоорганизация"),
                category = SurveyCategory.MOTIVATION
            ),
            // EXTERNAL доп.
            SurveyQuestion(
                text = "В каких условиях вы выполняли задачи сегодня?",
                options = listOf("Дома", "На работе / учёбе", "В дороге", "В шумной обстановке", "В спокойной обстановке"),
                category = SurveyCategory.EXTERNAL
            ),
            SurveyQuestion(
                text = "Как вы оцениваете свою ежедневную нагрузку?",
                options = listOf("Слишком лёгкая", "Комфортная", "Средняя", "Высокая", "Слишком высокая"),
                category = SurveyCategory.EXTERNAL
            ),
            // CONCENTRATION доп.
            SurveyQuestion(
                text = "Удалось ли вам сосредоточиться на работе сегодня?",
                options = listOf("Да, полностью", "В основном да", "Иногда отвлекался", "Нет"),
                category = SurveyCategory.CONCENTRATION
            )
        )
    }

    // НОВОЕ: перезапуск опроса — удаляем старые ответы и начинаем заново
    fun restartSurvey() {
        viewModelScope.launch {
            val today = LocalDate.now()
            // Удаляем старые ответы за сегодня
            surveyDao.deleteForDate(today)
            // Сбрасываем состояние и загружаем заново
            _uiState.value = SurveyUiState(isLoading = true)
            var tasks = dao.getForToday(today).first()
            if (tasks.isEmpty()) tasks = dao.getActive().first()
            val questions = buildQuestions(tasks)
            _uiState.value = SurveyUiState(questions = questions, isLoading = false)
        }
    }
}