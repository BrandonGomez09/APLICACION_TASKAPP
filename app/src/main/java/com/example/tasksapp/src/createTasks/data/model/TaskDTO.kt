package com.example.tasksapp.src.createTasks.data.model

import com.google.gson.annotations.SerializedName

data class TaskDTO (
    @SerializedName("idtasks")
    val id: Int,
    val name: String,
    val description: String,
)
