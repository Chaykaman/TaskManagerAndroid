package com.example.taskmanager.feature.habits.habitstats.components

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.taskmanager.data.local.entity.habit.DayCompletionCount
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.time.format.TextStyle
import java.util.Locale
import androidx.core.graphics.toColorInt
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter

@Composable
fun HabitLineChart(
    dailyCompletions: List<DayCompletionCount>,
    totalHabitsCount: Int,
    modifier: Modifier = Modifier
) {
    val textColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()
    val primaryColor = "#6650A4".toColorInt()

    val lineData = remember(dailyCompletions, totalHabitsCount) {
        if (dailyCompletions.isEmpty() || totalHabitsCount == 0) return@remember null

        val entries = dailyCompletions.mapIndexed { index, day ->
            // Переводим количество в процент от общего числа привычек
            val percentage = (day.completedCount.toFloat() / totalHabitsCount) * 100f
            Entry(index.toFloat(), percentage)
        }

        val dataSet = LineDataSet(entries, "").apply {
            color = primaryColor
            lineWidth = 2.5f
            setDrawCircles(true)
            circleRadius = 4f
            setCircleColor(primaryColor)
            circleHoleRadius = 2f
            setDrawValues(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER // плавная линия
            setDrawFilled(true)
            // Градиентная заливка под линией
            fillDrawable = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(
                    Color.argb(80, 102, 80, 164),
                    Color.TRANSPARENT
                )
            )
            highLightColor = Color.TRANSPARENT
        }

        LineData(dataSet)
    }

    val xLabels = remember(dailyCompletions) {
        dailyCompletions.map { day ->
            // Для месяца показываем число, для недели — день недели
            if (dailyCompletions.size > 7) {
                day.date.dayOfMonth.toString()
            } else {
                day.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("ru"))
                    .replaceFirstChar { it.uppercase() }
            }
        }
    }

    if (dailyCompletions.isEmpty()) {
        ChartEmptyState(modifier = modifier)
        return
    }

    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = false
                setTouchEnabled(false)
                setDrawGridBackground(false)
                setDrawBorders(false)
                extraBottomOffset = 8f

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
                    axisMinimum = 0f
                    axisMaximum = 100f
                    // Показываем значения как проценты
                    valueFormatter = PercentFormatter()
                    this.textColor = textColor
                    textSize = 11f
                }

                axisRight.isEnabled = false
                animateX(500)
            }
        },
        update = { chart ->
            chart.xAxis.valueFormatter = IndexAxisValueFormatter(xLabels)
            chart.data = lineData
            chart.invalidate()
        },
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}