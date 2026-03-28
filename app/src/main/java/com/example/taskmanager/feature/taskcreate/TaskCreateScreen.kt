package com.example.taskmanager.feature.taskcreate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskmanager.data.local.entity.Priority
import com.example.taskmanager.feature.tasklist.TaskListViewModel
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TaskCreateScreen(
    viewModel: TaskListViewModel = hiltViewModel(),
    defaultDate: LocalDate? = null,
    onBack: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var taskPriority by remember { mutableStateOf(Priority.PRIORITY_4) }
    var taskDate by remember { mutableStateOf<LocalDate?>(defaultDate) }
    var taskTime by remember { mutableStateOf<LocalTime?>(null) }

    TaskCreateContent(
        title = taskTitle,
        description = taskDescription,
        priority = taskPriority,
        dueDate = taskDate,
        dueTime = taskTime,
        focusRequester = focusRequester,
        onTitleChange = { newTitle -> taskTitle = newTitle },
        onDescriptionChange = { newDescription -> taskDescription = newDescription },
        onPriorityChange = { newPriority -> taskPriority = newPriority },
        onDateSelect = { newDueDate -> taskDate = newDueDate },
        onTimeSelect = { newDueTime ->
            taskTime = newDueTime
            if (newDueTime != null && taskDate == null) {
                taskDate = LocalDate.now()
            }
        },
        onSubmit = {
            viewModel.addTask(
                title = taskTitle,
                description = taskDescription,
                priority = taskPriority,
                dueDate = taskDate,
                dueTime = taskTime
            )
            onBack()
        }
    )
}