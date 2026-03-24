package com.example.taskmanager

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskmanager.data.local.entity.AppTheme
import com.example.taskmanager.data.repository.AppSettingsRepository
import com.example.taskmanager.feature.appsettings.AppSettingsViewModel
import com.example.taskmanager.feature.common.LocalFabAlignment
import com.example.taskmanager.navigation.Main
import com.example.taskmanager.ui.theme.TaskManagerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsRepository: AppSettingsRepository

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        var savedTheme: AppTheme
        runBlocking {
            savedTheme = settingsRepository.appTheme.first()
        }

        val viewModel: AppSettingsViewModel by viewModels()
        splashScreen.setKeepOnScreenCondition {
            viewModel.uiState.value.isLoading
        }

        setContent {
            val settingsUiState by viewModel.uiState.collectAsStateWithLifecycle()
            val theme = settingsUiState.appTheme ?: savedTheme

            val darkTheme = when (theme) {
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
                AppTheme.SYSTEM -> isSystemInDarkTheme()
            }

            TaskManagerTheme(darkTheme = darkTheme) {
                val windowInsetsController = WindowCompat
                    .getInsetsController(window, window.decorView)

                SideEffect {
                    windowInsetsController.isAppearanceLightStatusBars = !darkTheme
                    windowInsetsController.isAppearanceLightNavigationBars = !darkTheme
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = {
                        CompositionLocalProvider(
                            LocalFabAlignment provides settingsUiState.fabAlignment
                        ) {
                            Main()
                        }
                    }
                )
            }
        }
    }
}