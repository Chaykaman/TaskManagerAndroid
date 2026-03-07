package com.example.taskmanager.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay

@Composable
fun Main() {
    val navigationState = rememberNavigationState(
        startRoute = Route.Tasks,
        topLevelRoutes = TOP_LEVEL_ROUTES.keys
    )

    val navigator = remember { Navigator(navigationState) }

    val entryProvider = entryProvider {
        featureTasksSection()
        featureCalendarSection()
        featureMenuSection()
    }

    val bottomSheetStrategy = remember { BottomSheetSceneStrategy<NavKey>() }

    Scaffold(
        bottomBar = {
            NavigationBar {
                TOP_LEVEL_ROUTES.forEach { (key, value) ->
                    val isSelected = key == navigationState.topLevelRoute
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { navigator.navigate(key) },
                        icon = {
                            Icon(
                                imageVector = value.icon,
                                contentDescription = value.title
                            )
                        },
                        label = { Text(value.title) },
                    )
                }
            }
        }
    ) { outerPadding ->
        NavDisplay(
            modifier = Modifier.padding(bottom = outerPadding.calculateBottomPadding()),
            sceneStrategy = bottomSheetStrategy,
            entries = navigationState.toDecoratedEntries(entryProvider),
            onBack = { navigator.goBack() }
        )
    }
}