package com.example.tasksapp.src.createTasks.data.model

data class CreateTaskRequest(
    val name: String,
    val description: String,
    val id_user: Int
)