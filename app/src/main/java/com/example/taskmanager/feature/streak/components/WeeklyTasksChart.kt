package com.example.taskmanager.feature.streak.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.toColorInt
import com.example.taskmanager.data.local.entity.DayTaskCount
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeeklyTasksChart(
    weeklyTaskCounts: List<DayTaskCount>,
    minTasksPerDay: Int,
    modifier: Modifier = Modifier
) {
    val textColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()

    val today = LocalDate.now()
    val weekStart = today.with(DayOfWeek.MONDAY)

    val countsByDate = remember(weeklyTaskCounts) {
        weeklyTaskCounts.associate { it.date to it.completedCount }
    }

    val weekDays = remember(weekStart) {
        (0..6).map { weekStart.plusDays(it.toLong()) }
    }

    val barData = remember(weeklyTaskCounts, minTasksPerDay) {
        // Столбцы выполненных задач
        val entries = weekDays.mapIndexed { index, date ->
            BarEntry(index.toFloat(), (countsByDate[date] ?: 0).toFloat())
        }

        // Раскрашиваем столбцы: зелёный если выполнен минимум, серый если нет.
        val colors = weekDays.map { date ->
            val count = countsByDate[date] ?: 0
            if (count >= minTasksPerDay) {
                "#4CAF50".toColorInt() // зелёный — день засчитан
            } else {
                "#BDBDBD".toColorInt() // серый — не засчитан
            }
        }

        val dataSet = BarDataSet(entries, "").apply {
            this.colors = colors
            setDrawValues(true)
            valueTextSize = 10f
            valueTextColor = textColor
            // Показываем значение только если оно больше 0 — не захламляем нулями
            valueFormatter = object : ValueFormatter() {
                override fun getBarLabel(barEntry: BarEntry): String {
                    return if (barEntry.y > 0) barEntry.y.toInt().toString() else ""
                }
            }
            highLightAlpha = 0
        }

        BarData(dataSet).apply { barWidth = 0.6f }
    }

    // Линия минимума — горизонтальная пунктирная линия на уровне порога
    val limitLine = remember(minTasksPerDay) {
        LimitLine(minTasksPerDay.toFloat(), "").apply {
            lineWidth = 1.5f
            lineColor = "#FF5722".toColorInt()
            enableDashedLine(10f, 5f, 0f)
            labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
        }
    }

    val xLabels = remember(weekDays) {
        weekDays.map { date ->
            // Сегодня выделяем звёздочкой для ориентации
            val label = date.dayOfWeek
                .getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("ru"))
                .replaceFirstChar { it.uppercase() }
            if (date == today) "• $label" else label
        }
    }

    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = false
                setTouchEnabled(false)
                setDrawGridBackground(false)
                setDrawBorders(false)
                extraBottomOffset = 8f
                extraTopOffset = 16f

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                    granularity = 1f
                    this.textColor = textColor
                    textSize = 11f
                }

                axisLeft.apply {
                    setDrawGridLines(true)
                    gridColor = "#1A000000".toColorInt()
                    setDrawAxisLine(false)
                    granularity = 1f
                    axisMinimum = 0f
                    this.textColor = textColor
                    textSize = 11f
                    // Линия минимума
                    removeAllLimitLines()
                    addLimitLine(limitLine)
                    setDrawLimitLinesBehindData(false) // линия поверх столбцов
                }

                axisRight.isEnabled = false
                animateY(600)
            }
        },
        update = { chart ->
            chart.xAxis.valueFormatter = IndexAxisValueFormatter(xLabels)
            // Обновляем линию минимума при изменении настройки
            chart.axisLeft.removeAllLimitLines()
            chart.axisLeft.addLimitLine(limitLine)
            chart.data = barData
            chart.invalidate()
        },
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
    )
}