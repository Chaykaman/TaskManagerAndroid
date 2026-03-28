package com.example.taskmanager.feature.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.local.dao.Dao
import com.example.taskmanager.data.local.dao.SurveyDao
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
    val id: String = "",
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
            _uiState.value = SurveyUiState(
                questions = questions,
                isLoading = false
            )
        }
    }

    fun answerQuestion(answer: String) {
        val state = _uiState.value
        val currentQuestion = state.questions[state.currentIndex]

        val newAnswers = state.answers.toMutableMap()
        newAnswers[state.currentIndex] = answer

        val newAnswerById = state.answerById.toMutableMap()
        if (currentQuestion.id.isNotEmpty()) {
            newAnswerById[currentQuestion.id] = answer
        }

        val newQuestions = state.questions.toMutableList()
        if (currentQuestion.id == "concentration_freq") {
            val branchIndex = newQuestions.indexOfFirst { it.id == "concentration_branch" }
            if (branchIndex != -1) {
                newQuestions[branchIndex] = if (answer == "Совсем нет") {
                    SurveyQuestion(
                        id = "concentration_no_distract",
                        text = "Что помогло вам сегодня не отвлекаться и сосредоточиться на задачах?",
                        options = listOf("Отключение уведомлений", "Дисциплина", "Спокойная обстановка", "Чувство ответственности", "Личная заинтересованность"),
                        category = SurveyCategory.CONCENTRATION
                    )
                } else {
                    SurveyQuestion(
                        id = "concentration_what",
                        text = "Что чаще всего отвлекало вас сегодня?",
                        options = listOf("Уведомления и телефон", "Другие задачи", "Люди вокруг", "Усталость", "Соцсети"),
                        category = SurveyCategory.CONCENTRATION
                    )
                }
            }
        }

        val nextIndex = state.currentIndex + 1
        val isFinished = nextIndex >= newQuestions.size

        _uiState.value = state.copy(
            questions = newQuestions,
            answers = newAnswers,
            answerById = newAnswerById,
            currentIndex = if (isFinished) state.currentIndex else nextIndex,
            isFinished = isFinished
        )

        if (isFinished) {
            saveResults(newQuestions, newAnswers)
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

    private fun buildQuestions(tasks: List<Task>): List<SurveyQuestion> {
        val overdueCount = tasks.count { it.isOverdue() }

        val selected = mutableListOf<SurveyQuestion>()

        selected.add(SurveyQuestion(
            id = "productivity_score",
            text = "Как вы оцениваете свою продуктивность сегодня?",
            options = listOf("Отлично", "Хорошо", "Удовлетворительно", "Плохо"),
            category = SurveyCategory.PRODUCTIVITY
        ))
        selected.add(SurveyQuestion(
            id = "productivity_time",
            text = "В какое время дня вы были наиболее продуктивны сегодня?",
            options = listOf("Утро", "День", "Вечер"),
            category = SurveyCategory.PRODUCTIVITY
        ))
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
        selected.add(SurveyQuestion(
            id = "task_obstacles",
            text = "Что чаще всего мешало вам выполнять задачи сегодня?",
            options = listOf("Отвлекающие факторы", "Усталость", "Нехватка мотивации", "Плохое планирование", "Ничего не мешало"),
            category = SurveyCategory.TASK_COMPLETION
        ))
        selected.add(SurveyQuestion(
            id = "concentration_freq",
            text = "Часто ли вы отвлекались во время работы сегодня?",
            options = listOf("Совсем нет", "Редко", "Иногда", "Часто", "Постоянно"),
            category = SurveyCategory.CONCENTRATION
        ))
        selected.add(SurveyQuestion(
            id = "concentration_branch",
            text = "", // заполнится динамически
            options = emptyList(),
            category = SurveyCategory.CONCENTRATION
        ))
        selected.add(SurveyQuestion(
            id = "planning_match",
            text = "Соответствовал ли ваш план дня реальности?",
            options = listOf("Полностью", "В основном да", "Частично", "Совсем нет", "Не планировал"),
            category = SurveyCategory.PLANNING
        ))
        selected.add(SurveyQuestion(
            id = "tools_used",
            text = "Какие инструменты вы использовали для выполнения задач сегодня?",
            options = listOf("Искусственный интеллект", "Поиск в интернете", "Специализированные приложения", "Помощь других людей", "Не использовал доп. инструменты"),
            category = SurveyCategory.TOOLS
        ))
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

        if (overdueCount > 0) {
            selected.add(SurveyQuestion(
                id = "overdue_reason",
                text = "Есть просроченные задачи. Почему не успели их выполнить?",
                options = listOf("Не хватило времени", "Задачи оказались сложнее", "Потерял интерес / мотивацию", "Были более важные задачи", "Внешние обстоятельства"),
                category = SurveyCategory.OVERDUE
            ))
        }

        val extraPool = buildExtraPool()
        val usedTexts = selected.map { it.text }.toSet()

        for (question in extraPool.shuffled()) {
            if (selected.size >= 15) break
            if (question.text !in usedTexts) {
                selected.add(question)
            }
        }

        return selected
    }

    private fun buildExtraPool(): List<SurveyQuestion> {
        return listOf(
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
            SurveyQuestion(
                text = "Удалось ли вам соблюдать баланс работы и отдыха?",
                options = listOf("Да", "Скорее да", "Скорее нет", "Нет"),
                category = SurveyCategory.WELLBEING
            ),
            SurveyQuestion(
                text = "Как вы себя чувствуете в конце дня?",
                options = listOf("Бодро", "Нормально", "Немного устал", "Очень устал"),
                category = SurveyCategory.WELLBEING
            ),
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
            SurveyQuestion(
                text = "Что помогло вам выполнить задачи сегодня?",
                options = listOf("Чёткий план", "Напоминания", "Помощь других людей", "Использование технологий / ИИ", "Самоорганизация"),
                category = SurveyCategory.MOTIVATION
            ),
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
            SurveyQuestion(
                text = "Удалось ли вам сосредоточиться на работе сегодня?",
                options = listOf("Да, полностью", "В основном да", "Иногда отвлекался", "Нет"),
                category = SurveyCategory.CONCENTRATION
            )
        )
    }

    fun restartSurvey() {
        viewModelScope.launch {
            val today = LocalDate.now()
            surveyDao.deleteForDate(today)
            _uiState.value = SurveyUiState(isLoading = true)
            var tasks = dao.getForToday(today).first()
            if (tasks.isEmpty()) tasks = dao.getActive().first()
            val questions = buildQuestions(tasks)
            _uiState.value = SurveyUiState(
                questions = questions,
                isLoading = false
            )
        }
    }
}