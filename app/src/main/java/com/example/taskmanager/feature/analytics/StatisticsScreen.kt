package com.example.taskmanager.feature.analytics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskmanager.data.local.entity.Priority
import com.example.taskmanager.feature.common.ScreenScaffold

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun StatisticsScreen(
    onBack: () -> Unit,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    ScreenScaffold(
        topBar = {
            TopAppBar(
                title = { Text("Статистика") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ===== БЛОК 1: Общая статистика задач =====
                SummaryCards(state = state)

                // ===== БЛОК 2: Выполненные задачи по дням =====
                // ProductivityChart(productivityByDay = state.productivityByDay) //

                // ===== БЛОК 3: По приоритетам =====
                PriorityDistributionChart(distribution = state.distributionByPriority)

                // ===== БЛОК 4: Среднее время =====
                // state.averageCompletionTime?.let { AverageTimeCard(hours = it) }

                // ===== БЛОКИ ИЗ ОПРОСОВ =====
                if (state.hasSurveyData) {
                    Text(
                        text = "Аналитика опросов (за последние 7 дней)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // PRODUCTIVITY — оценка продуктивности по дням
                    if (state.productivityScoreByDay.isNotEmpty()) {
                        LineChartCard(
                            title = "Оценка продуктивности по дням",
                            entries = state.productivityScoreByDay,
                            color = Color(0xFF2196F3),
                            minValue = 1f, maxValue = 4f,
                            labels = listOf("Плохо", "Удовл.", "Хорошо", "Отлично")
                        )
                    }

                    // PRODUCTIVITY — время суток
                    if (state.productivityTimeOfDay.values.any { it > 0 }) {
                        TimeOfDayChart(timeOfDay = state.productivityTimeOfDay)
                    }

                    // WELLBEING — самочувствие
                    if (state.wellbeingByDay.isNotEmpty()) {
                        LineChartCard(
                            title = "Самочувствие по дням",
                            entries = state.wellbeingByDay,
                            color = Color(0xFF4CAF50),
                            minValue = 1f, maxValue = 4f,
                            labels = listOf("Очень устал", "Устал", "Нормально", "Энергично")
                        )
                    }

                    // WELLBEING — стресс
                    if (state.stressByDay.isNotEmpty()) {
                        LineChartCard(
                            title = "Уровень стресса по дням",
                            entries = state.stressByDay,
                            color = Color(0xFFF44336),
                            minValue = 1f, maxValue = 4f,
                            labels = listOf("Нет", "Лёгкий", "Умеренный", "Сильный")
                        )
                    }

                    // MOTIVATION — эмоции (круговая диаграмма)
                    if (state.emotionDistribution.isNotEmpty()) {
                        EmotionPieChart(distribution = state.emotionDistribution)
                    }

                    // MOTIVATION — топ мотивации
                    if (state.topMotivations.isNotEmpty()) {
                        TopListCard(
                            title = "Главные источники мотивации",
                            items = state.topMotivations,
                            color = Color(0xFFFF9800)
                        )
                    } else {
                        EmptySurveyBlock(
                            title = "Главные источники мотивации",
                            message = "Данных о мотивации пока недостаточно 📊"
                        )
                    }

                    // TASK_COMPLETION — причины невыполнения
                    if (state.topFailureReasons.isNotEmpty()) {
                        TopListCard(
                            title = "Что мешает выполнять задачи",
                            items = state.topFailureReasons,
                            color = Color(0xFFF44336)
                        )
                    } else {
                        EmptySurveyBlock(
                            title = "Что мешает выполнять задачи",
                            message = "Вам ничего не мешало выполнять задачи! 🎉"
                        )
                    }

                    // CONCENTRATION — источники отвлечений
                    if (state.topDistractions.isNotEmpty()) {
                        TopListCard(
                            title = "Главные источники отвлечений",
                            items = state.topDistractions,
                            color = Color(0xFF9C27B0)
                        )
                    } else {
                        EmptySurveyBlock(
                            title = "Главные источники отвлечений",
                            message = "Вы не отвлекались во время работы! 🎯"
                        )
                    }

                    // TOOLS — инструменты
                    if (state.topTools.isNotEmpty()) {
                        TopListCard(
                            title = "Часто используемые инструменты",
                            items = state.topTools,
                            color = Color(0xFF2196F3)
                        )
                    } else {
                        EmptySurveyBlock(
                            title = "Часто используемые инструменты",
                            message = "Вы справлялись без дополнительных инструментов 💪"
                        )
                    }

                } else {
                    NoSurveyDataCard()
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ===== ОБЩАЯ СТАТИСТИКА =====

@Composable
private fun SummaryCards(state: StatisticsUiState) {
    Text(
        text = "Общая статистика задач",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCard(Modifier.weight(1f), "Всего", state.totalTasks.toString(), MaterialTheme.colorScheme.primary)
        StatCard(Modifier.weight(1f), "Активных", state.activeTasks.toString(), MaterialTheme.colorScheme.tertiary)
        StatCard(Modifier.weight(1f), "Выполнено", state.completedTasks.toString(), Color(0xFF4CAF50))
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Процент выполнения", style = MaterialTheme.typography.bodyLarge)
            Text(
                "${"%.1f".format(state.completionRate)}%",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun StatCard(modifier: Modifier, title: String, value: String, color: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = color)
            Text(title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        }
    }
}

// ===== СТОЛБЧАТЫЙ ГРАФИК (выполненные задачи по дням) =====

// ИЗМЕНЕНО: добавлена ось Y с количеством задач
@Composable
private fun ProductivityChart(productivityByDay: List<Pair<String, Int>>) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Выполнено задач за 7 дней", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            if (productivityByDay.isEmpty() || productivityByDay.all { it.second == 0 }) {
                EmptyChartBox()
            } else {
                val maxValue = productivityByDay.maxOf { it.second }.coerceAtLeast(1)
                val barColor = MaterialTheme.colorScheme.primary

                Row(modifier = Modifier.fillMaxWidth()) {
                    // Ось Y
                    Column(
                        modifier = Modifier.height(120.dp).width(24.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf(maxValue, maxValue / 2, 0).forEach { value ->
                            Text(
                                text = value.toString(),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Canvas(modifier = Modifier.weight(1f).height(120.dp)) {
                        drawBarChart(productivityByDay, maxValue, barColor)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 28.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    productivityByDay.forEach { (date, _) ->
                        Text(date, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawBarChart(data: List<Pair<String, Int>>, maxValue: Int, color: Color) {
    val barCount = data.size
    val spacing = size.width * 0.05f / (barCount + 1)
    val barWidth = (size.width - spacing * (barCount + 1)) / barCount
    data.forEachIndexed { index, (_, value) ->
        val barHeight = (value.toFloat() / maxValue) * size.height
        val left = spacing + index * (barWidth + spacing)
        drawRoundRect(color = color, topLeft = Offset(left, size.height - barHeight), size = Size(barWidth, barHeight), cornerRadius = CornerRadius(8f, 8f))
    }
}

// ===== ЛИНЕЙНЫЙ ГРАФИК =====

@Composable
private fun LineChartCard(
    title: String,
    entries: List<WellbeingEntry>,
    color: Color,
    minValue: Float,
    maxValue: Float,
    labels: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("На основе ответов опроса", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.height(120.dp).width(80.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    labels.reversed().forEach {
                        Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Canvas(modifier = Modifier.weight(1f).height(120.dp)) {
                    drawLineChart(entries, minValue, maxValue, color)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 88.dp),
                horizontalArrangement = if (entries.size == 1)
                    Arrangement.Center
                else
                    Arrangement.SpaceBetween
            ) {
                entries.forEach { entry ->
                    Text(
                        entry.date,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawLineChart(
    entries: List<WellbeingEntry>,
    minValue: Float,
    maxValue: Float,
    color: Color
) {
    if (entries.isEmpty()) return
    val range = maxValue - minValue

    // если одна точка — рисуем просто точку по центру
    if (entries.size == 1) {
        val x = size.width / 2f
        val y = size.height - ((entries[0].score - minValue) / range) * size.height
        val center = Offset(x, y)
        drawCircle(color = color, radius = 8f, center = center)
        drawCircle(color = Color.White, radius = 4f, center = center)
        return
    }

    val stepX = size.width / (entries.size - 1)
    val points = entries.mapIndexed { index, entry ->
        Offset(index * stepX, size.height - ((entry.score - minValue) / range) * size.height)
    }
    val path = Path().apply {
        moveTo(points.first().x, points.first().y)
        for (i in 1 until points.size) {
            val controlX = (points[i - 1].x + points[i].x) / 2f
            cubicTo(controlX, points[i - 1].y, controlX, points[i].y, points[i].x, points[i].y)
        }
    }
    drawPath(path = path, color = color, style = Stroke(width = 4f))
    points.forEach {
        drawCircle(color = color, radius = 6f, center = it)
        drawCircle(color = Color.White, radius = 3f, center = it)
    }
}

// ===== ВРЕМЯ СУТОК (горизонтальная диаграмма) =====

@Composable
private fun TimeOfDayChart(timeOfDay: Map<String, Int>) {
    val total = timeOfDay.values.sum().coerceAtLeast(1)
    val colors = mapOf(
        "Утром" to Color(0xFFFF9800),
        "Днём" to Color(0xFF2196F3),
        "Вечером" to Color(0xFF9C27B0)
    )
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Когда вы наиболее продуктивны",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                "На основе ответов опроса",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            listOf("Утром", "Днем", "Вечером").forEach { label ->
                val count = timeOfDay[label] ?: 0
                val fraction = count.toFloat() / total
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // уменьшена ширина подписи и размер шрифта
                    Text(
                        label,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.width(44.dp),
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // ИЗМЕНЕНО: уменьшена высота столбика с 24 до 18
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(18.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction)
                                .height(18.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(colors[label] ?: Color.Gray)
                        )
                    }
                }
            }
        }
    }
}

// ===== КРУГОВАЯ ДИАГРАММА ЭМОЦИЙ =====

@Composable
private fun EmotionPieChart(distribution: Map<String, Int>) {
    val emotionColors = mapOf(
        "Удовлетворение" to Color(0xFF4CAF50),
        "Облегчение" to Color(0xFF2196F3),
        "Усталость" to Color(0xFFFF9800),
        "Безразличие" to Color(0xFF9E9E9E),
        "Раздражение" to Color(0xFFF44336)
    )
    val total = distribution.values.sum().toFloat().coerceAtLeast(1f)
    val entries = distribution.entries.toList()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Эмоции после рабочего дня", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("На основе ответов опроса", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Круговая диаграмма
                Canvas(modifier = Modifier.size(120.dp)) {
                    var startAngle = -90f
                    entries.forEach { (emotion, count) ->
                        val sweep = (count / total) * 360f
                        drawArc(
                            color = emotionColors[emotion] ?: Color.Gray,
                            startAngle = startAngle,
                            sweepAngle = sweep,
                            useCenter = true,
                            topLeft = Offset(0f, 0f),
                            size = Size(size.width, size.height)
                        )
                        startAngle += sweep
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                // Легенда
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    entries.forEach { (emotion, count) ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(10.dp).clip(CircleShape).background(emotionColors[emotion] ?: Color.Gray)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "$emotion ($count)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}


// ===== РАСПРЕДЕЛЕНИЕ ПО ПРИОРИТЕТАМ =====

@Composable
private fun PriorityDistributionChart(distribution: Map<Priority, Int>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("По приоритетам", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            distribution.entries.forEach { (priority, count) ->
                DistributionRow(label = priority.label, count = count, total = distribution.values.sum(), color = priority.color)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun DistributionRow(label: String, count: Int, total: Int, color: Color) {
    val fraction = if (total == 0) 0f else count.toFloat() / total
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.width(100.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Box(modifier = Modifier.weight(1f).height(8.dp).clip(RoundedCornerShape(4.dp)).background(MaterialTheme.colorScheme.surface)) {
            Box(modifier = Modifier.fillMaxWidth(fraction).height(8.dp).clip(RoundedCornerShape(4.dp)).background(color))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(count.toString(), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}

// ===== СРЕДНЕЕ ВРЕМЯ =====

@Composable
private fun AverageTimeCard(hours: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Среднее время выполнения", style = MaterialTheme.typography.bodyLarge)
            Text("${"%.1f".format(hours)} ч", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
        }
    }
}

// ===== ТОП СПИСОК =====

@Composable
private fun TopListCard(title: String, items: List<Pair<String, Int>>, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("На основе ответов опроса", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(12.dp))
            val maxCount = items.maxOf { it.second }.coerceAtLeast(1)
            items.forEachIndexed { index, (label, count) ->
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("${index + 1}", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = color, modifier = Modifier.width(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(label, style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)).background(MaterialTheme.colorScheme.surfaceVariant)) {
                            Box(modifier = Modifier.fillMaxWidth(count.toFloat() / maxCount).height(6.dp).clip(RoundedCornerShape(3.dp)).background(color))
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(count.toString(), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = color)
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

// ===== НЕТ ДАННЫХ ОПРОСА =====

@Composable
private fun NoSurveyDataCard() {
    Card(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("📊", style = MaterialTheme.typography.displaySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Нет данных опросов", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Пройдите опрос дня чтобы увидеть аналитику самочувствия, стресса и продуктивности",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ===== ПУСТОЙ ГРАФИК =====

@Composable
private fun EmptyChartBox() {
    Box(modifier = Modifier.fillMaxWidth().height(120.dp), contentAlignment = Alignment.Center) {
        Text("Нет данных за этот период", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// блок с позитивным сообщением когда топ пустой
@Composable
private fun EmptySurveyBlock(title: String, message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                "На основе ответов опроса",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}