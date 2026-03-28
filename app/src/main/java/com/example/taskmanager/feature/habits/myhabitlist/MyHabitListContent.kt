package com.example.taskmanager.feature.habits.myhabitlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.local.entity.habit.Habit
import com.example.taskmanager.feature.habits.myhabitlist.components.MyHabitList

@Composable
fun MyHabitListContent(
    innerPadding: PaddingValues,
    selectedTab: MyHabitListTab,
    onTabSelected: (MyHabitListTab) -> Unit,
    habits: List<Habit>,
    onHabitClick: (Int) -> Unit,
    onToggleArchiveHabit: (Habit) -> Unit,
    onRemoveHabit: (Habit) -> Unit,
) {
    Column(modifier = Modifier.padding(innerPadding)) {
        SecondaryTabRow(selectedTabIndex = MyHabitListTab.entries.indexOf(selectedTab)) {
            MyHabitListTab.entries.forEach { tab ->
                Tab(
                    text = { Text(text = tab.title) },
                    selected = selectedTab == tab,
                    onClick = { onTabSelected(tab) }
                )
            }
        }

        Text(
            text = when (selectedTab) {
                MyHabitListTab.ACTIVE -> "Активные привычки отображаются на главной странице " +
                        "и обновляются каждый день, чтобы вы могли отслеживать и выполнять их регулярно."
                MyHabitListTab.ARCHIVED -> "Архивированные привычки не отображаются на главной " +
                        "странице — вы можете вернуться к ним позже, когда решите возобновить."
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )

        MyHabitList(
            habits = habits,
            onHabitClick = onHabitClick,
            onToggleArchive = onToggleArchiveHabit,
            onRemove = onRemoveHabit,
        )
    }
}