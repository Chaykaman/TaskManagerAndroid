package com.example.taskmanager.feature.habits.habitstats.components

import android.graphics.Color
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
import com.example.taskmanager.data.local.entity.habit.DayCompletionCount
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HabitBarChart(
    dailyCompletions: List<DayCompletionCount>,
    modifier: Modifier = Modifier
) {
    val textColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()

    val barData = remember(dailyCompletions) {
        if (dailyCompletions.isEmpty()) return@remember null

        val entries = dailyCompletions.mapIndexed { index, day ->
            BarEntry(index.toFloat(), day.completedCount.toFloat())
        }

        val dataSet = BarDataSet(entries, "").apply {
            color = "#6650A4".toColorInt()
            setDrawValues(false)
            highLightAlpha = 0
        }

        BarData(dataSet).apply {
            barWidth = 0.6f
        }
    }

    val xLabels = remember(dailyCompletions) {
        dailyCompletions.map { day ->
            day.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("ru"))
                .replaceFirstChar { it.uppercase() }
        }
    }

    if (dailyCompletions.isEmpty()) {
        ChartEmptyState(modifier = modifier)
        return
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
                }

                axisRight.isEnabled = false
                animateY(500)
            }
        },
        update = { chart ->
            chart.xAxis.valueFormatter = IndexAxisValueFormatter(xLabels)
            chart.data = barData
            chart.invalidate()
        },
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}