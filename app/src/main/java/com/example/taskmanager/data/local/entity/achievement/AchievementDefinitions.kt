package com.example.taskmanager.data.local.entity.achievement

object AchievementDefinitions {

    val all: List<AchievementDefinition> = listOf(

        // ===== Задачи: создание =====
        AchievementDefinition(
            id = "tasks_created_10",
            title = "Начало пути",
            description = "Создайте 10 задач",
            iconName = "add_task",
            level = AchievementLevel.BRONZE,
            trigger = AchievementTrigger.TASK_CREATED,
            progressType = AchievementProgressType.Counter(10)
        ),
        AchievementDefinition(
            id = "tasks_created_20",
            title = "Организатор",
            description = "Создайте 20 задач",
            iconName = "add_task",
            level = AchievementLevel.SILVER,
            trigger = AchievementTrigger.TASK_CREATED,
            progressType = AchievementProgressType.Counter(20)
        ),
        AchievementDefinition(
            id = "tasks_created_50",
            title = "Мастер планирования",
            description = "Создайте 50 задач",
            iconName = "add_task",
            level = AchievementLevel.GOLD,
            trigger = AchievementTrigger.TASK_CREATED,
            progressType = AchievementProgressType.Counter(50)
        ),

        // ===== Задачи: выполнение =====
        AchievementDefinition(
            id = "tasks_completed_10",
            title = "Исполнитель",
            description = "Выполните 10 задач",
            iconName = "task_alt",
            level = AchievementLevel.BRONZE,
            trigger = AchievementTrigger.TASK_COMPLETED,
            progressType = AchievementProgressType.Counter(10)
        ),
        AchievementDefinition(
            id = "tasks_completed_20",
            title = "Продуктивный",
            description = "Выполните 20 задач",
            iconName = "task_alt",
            level = AchievementLevel.SILVER,
            trigger = AchievementTrigger.TASK_COMPLETED,
            progressType = AchievementProgressType.Counter(20)
        ),
        AchievementDefinition(
            id = "tasks_completed_50",
            title = "Чемпион задач",
            description = "Выполните 50 задач",
            iconName = "task_alt",
            level = AchievementLevel.GOLD,
            trigger = AchievementTrigger.TASK_COMPLETED,
            progressType = AchievementProgressType.Counter(50)
        ),

        // ===== Привычки: создание =====
        AchievementDefinition(
            id = "habits_created_5",
            title = "Формирование",
            description = "Создайте 5 привычек",
            iconName = "repeat",
            level = AchievementLevel.BRONZE,
            trigger = AchievementTrigger.HABIT_CREATED,
            progressType = AchievementProgressType.Counter(5)
        ),
        AchievementDefinition(
            id = "habits_created_10",
            title = "Система привычек",
            description = "Создайте 10 привычек",
            iconName = "repeat",
            level = AchievementLevel.SILVER,
            trigger = AchievementTrigger.HABIT_CREATED,
            progressType = AchievementProgressType.Counter(10)
        ),

        // ===== Привычки: все за день =====
        AchievementDefinition(
            id = "all_habits_completed",
            title = "Идеальный день",
            description = "Выполните все привычки за один день",
            iconName = "mood",
            level = AchievementLevel.GOLD,
            trigger = AchievementTrigger.ALL_HABITS_COMPLETED,
            progressType = AchievementProgressType.Boolean
        ),

        // ===== Стрики =====
        AchievementDefinition(
            id = "streak_10",
            title = "На волне",
            description = "Достигните серии 10 дней",
            iconName = "local_fire_department",
            level = AchievementLevel.BRONZE,
            trigger = AchievementTrigger.STREAK_REACHED,
            progressType = AchievementProgressType.Counter(10)
        ),
        AchievementDefinition(
            id = "streak_20",
            title = "Несломимый",
            description = "Достигните серии 20 дней",
            iconName = "local_fire_department",
            level = AchievementLevel.SILVER,
            trigger = AchievementTrigger.STREAK_REACHED,
            progressType = AchievementProgressType.Counter(20)
        ),
        AchievementDefinition(
            id = "streak_30",
            title = "Легенда",
            description = "Достигните серии 30 дней",
            iconName = "local_fire_department",
            level = AchievementLevel.GOLD,
            trigger = AchievementTrigger.STREAK_REACHED,
            progressType = AchievementProgressType.Counter(30)
        )
    )

    // Быстрый доступ по id - используется в AchievementManager
    val byId: Map<String, AchievementDefinition> = all.associateBy { it.id }
}