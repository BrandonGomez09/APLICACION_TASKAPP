package com.example.tasksapp.src.viewTasks.data.model

import com.google.gson.annotations.SerializedName

data class ViewTaskDTO(
    @SerializedName("idtasks")
    val id: Int,
    val name: String,
    val description: String,

    // --- LÍNEA ACTUALIZADA ---
    val imageUrl: String? // Le decimos a la app que ahora puede recibir una URL de imagen opcional
)