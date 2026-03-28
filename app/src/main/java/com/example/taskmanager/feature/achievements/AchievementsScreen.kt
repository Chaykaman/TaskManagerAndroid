package com.example.taskmanager.feature.achievements

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.taskmanager.feature.common.ScreenScaffold
import com.example.taskmanager.feature.common.topappbar.ChildScreenTopAppBar

@Composable
fun AchievementsScreen(
    onBack: () -> Unit,
) {
    ScreenScaffold(
        topBar = {
            ChildScreenTopAppBar(
                title = "Достижения",
                onBack = onBack
            )
        }
    ) {
        Text("Achievements", modifier = Modifier.padding(it))
    }
}