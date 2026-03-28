package com.example.taskmanager.feature.productivity

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.taskmanager.feature.common.ScreenScaffold
import com.example.taskmanager.feature.common.topappbar.ChildScreenTopAppBar

@Composable
fun ProductivityScreen(
    onBack: () -> Unit,
) {
    ScreenScaffold(
        topBar = {
            ChildScreenTopAppBar(
                title = "Продуктивность",
                onBack = onBack
            )
        }
    ) {
        Text("Productivity", modifier = Modifier.padding(it))
    }
}