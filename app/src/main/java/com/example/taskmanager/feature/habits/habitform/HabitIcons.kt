package com.example.taskmanager.feature.habits.habitform

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.DirectionsRun
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Bed
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Recycling
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material.icons.rounded.SportsEsports
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector

object HabitIcons {
    data class HabitIcon(val name: String, val icon: ImageVector)

    val available = listOf(
        HabitIcon("fitness_center", Icons.Rounded.FitnessCenter),
        HabitIcon("book", Icons.Rounded.Book),
        HabitIcon("water_drop", Icons.Rounded.WaterDrop),
        HabitIcon("bed", Icons.Rounded.Bed),
        HabitIcon("restaurant", Icons.Rounded.Restaurant),
        HabitIcon("directions_run", Icons.AutoMirrored.Rounded.DirectionsRun),
        HabitIcon("self_improvement", Icons.Rounded.SelfImprovement),
        HabitIcon("music_note", Icons.Rounded.MusicNote),
        HabitIcon("sports_esports", Icons.Rounded.SportsEsports),
        HabitIcon("code", Icons.Rounded.Code),
        HabitIcon("school", Icons.Rounded.School),
        HabitIcon("brush", Icons.Rounded.Brush),
        HabitIcon("favorite", Icons.Rounded.Favorite),
        HabitIcon("recycling", Icons.Rounded.Recycling),
        HabitIcon("star", Icons.Rounded.Star)
    )

    fun getIcon(name: String): ImageVector =
        available.find { it.name == name }?.icon ?: Icons.Default.Star
}