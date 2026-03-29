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
import com.example.taskmanager.data.local.entity.habit.HabitStatEntry
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import androidx.core.graphics.toColorInt

@Composable
fun HabitPieChart(
    habitStats: List<HabitStatEntry>,
    modifier: Modifier = Modifier
) {
    val textColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()

    // Берём топ-5 для читаемости — больше сегментов делают диаграмму перегруженной
    val topStats = remember(habitStats) {
        habitStats.take(5).filter { it.completedDays > 0 }
    }

    val pieData = remember(topStats) {
        if (topStats.isEmpty()) return@remember null

        val entries = topStats.map { stat ->
            PieEntry(stat.completedDays.toFloat(), stat.habitTitle)
        }

        val colors = topStats.map { stat ->
            // Используем цвет самой привычки из нашей модели
            "#${stat.color.and(0xFFFFFF).toString(16).padStart(6, '0')}".toColorInt()
        }

        val dataSet = PieDataSet(entries, "").apply {
            this.colors = colors
            sliceSpace = 3f
            selectionShift = 0f
            setDrawValues(false)
        }

        PieData(dataSet)
    }

    if (topStats.isEmpty()) {
        ChartEmptyState(modifier = modifier)
        return
    }

    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                description.isEnabled = false
                isDrawHoleEnabled = true
                holeRadius = 58f
                transparentCircleRadius = 62f
                setHoleColor(Color.TRANSPARENT)
                setTransparentCircleAlpha(0)
                setDrawCenterText(true)
                centerText = "Топ\nпривычек"
                setCenterTextSize(13f)
                setCenterTextColor(textColor)
                setDrawEntryLabels(false)
                legend.apply {
                    isEnabled = true
                    this.textColor = textColor
                    textSize = 11f
                    form = Legend.LegendForm.CIRCLE
                    horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                    verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                    orientation = Legend.LegendOrientation.HORIZONTAL
                    setDrawInside(false)
                    isWordWrapEnabled = true
                }
                setTouchEnabled(false)
                animateY(600)
            }
        },
        update = { chart ->
            chart.data = pieData
            chart.invalidate()
        },
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
    )
}