package com.example.taskmanager.feature.habits.habitstats.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.taskmanager.feature.habits.habitstats.StatsPeriod

@Composable
fun PeriodSelector(
    selectedPeriod: StatsPeriod,
    onPeriodSelected: (StatsPeriod) -> Unit
) {
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        StatsPeriod.entries.forEachIndexed { index, period ->
            SegmentedButton(
                selected = selectedPeriod == period,
                onClick = { onPeriodSelected(period) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = StatsPeriod.entries.size
                )
            ) {
                Text(
                    text = when (period) {
                        StatsPeriod.WEEK -> "Неделя"
                        StatsPeriod.MONTH -> "Месяц"
                    }
                )
            }
        }
    }
}