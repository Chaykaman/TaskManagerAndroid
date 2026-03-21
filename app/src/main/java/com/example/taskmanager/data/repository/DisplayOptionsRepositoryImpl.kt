package com.example.taskmanager.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.taskmanager.data.local.entity.SortingDirection
import com.example.taskmanager.data.local.entity.SortingField
import com.example.taskmanager.data.local.entity.TaskFiltering
import com.example.taskmanager.data.local.entity.TaskGrouping
import com.example.taskmanager.data.local.entity.TaskSorting
import com.example.taskmanager.data.logger.TaskLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DisplayOptionsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : DisplayOptionsRepository {

    init {
        TaskLogger.i("[DisplayOptionsRepositoryImpl] Инициализирован")
    }

    private object Keys {
        val FILTERING = stringPreferencesKey("filtering")
        val SORTING_FIELD = stringPreferencesKey("sorting_field")
        val SORTING_DIRECTION = stringPreferencesKey("sorting_direction")
        val GROUPING = stringPreferencesKey("grouping")
    }

    // Поле фильтрации
    override val filtering: Flow<TaskFiltering> = dataStore.data.map { preferences ->
        preferences[Keys.FILTERING]
            ?.let { runCatching { TaskFiltering.valueOf(it) }.getOrNull() }
            ?: TaskFiltering.ALL
    }

    // Поля сортировки
    override val sorting: Flow<TaskSorting> = dataStore.data.map { preferences ->
        val field = preferences[Keys.SORTING_FIELD]
            ?.let { runCatching { SortingField.valueOf(it) }.getOrNull() }
            ?: SortingField.ID

        val direction = preferences[Keys.SORTING_DIRECTION]
            ?.let { runCatching { SortingDirection.valueOf(it) }.getOrNull() }
            ?: SortingDirection.ASC

        TaskSorting(field, direction)
    }

    // Поле группировки
    override val grouping: Flow<TaskGrouping> = dataStore.data.map { preferences ->
        preferences[Keys.GROUPING]
            ?.let { runCatching { TaskGrouping.valueOf(it) }.getOrNull() }
            ?: TaskGrouping.NONE
    }

    /**
     * Обновление поля фильтрации
     * @param filtering Новое значение поля фильтрации
     */
    override suspend fun saveFiltering(filtering: TaskFiltering) {
        dataStore.edit { preferences ->
            preferences[Keys.FILTERING] = filtering.name
        }

        TaskLogger.i(
            "[DisplayOptionsRepositoryImpl] Сохранено ${Keys.FILTERING.name}=${filtering.name}"
        )
    }

    /**
     * Обновление поля сортировки
     * @param sorting Новое значение поля сортировки
     */
    override suspend fun saveSorting(sorting: TaskSorting) {
        dataStore.edit { preferences ->
            preferences[Keys.SORTING_FIELD] = sorting.field.name
            preferences[Keys.SORTING_DIRECTION] = sorting.direction.name
        }

        TaskLogger.i(
            "[DisplayOptionsRepositoryImpl] Сохранено " +
                    "${Keys.SORTING_FIELD.name}=${sorting.field.name}, " +
                    "${Keys.SORTING_DIRECTION.name}=${sorting.direction.name}"
        )
    }

    /**
     * Обновление поля группировки
     * @param grouping Новое значение поля группировки
     */
    override suspend fun saveGrouping(grouping: TaskGrouping) {
        dataStore.edit { preferences ->
            preferences[Keys.GROUPING] = grouping.name
        }

        TaskLogger.i(
            "[DisplayOptionsRepositoryImpl] Сохранено ${Keys.GROUPING.name}=${grouping.name}"
        )
    }
}