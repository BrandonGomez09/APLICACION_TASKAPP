package com.example.tasksapp.src.createTasks.domain

import android.content.Context
import android.net.Uri
import com.example.tasksapp.src.createTasks.data.model.TaskDTO
import com.example.tasksapp.src.createTasks.data.repository.TaskRepository
import retrofit2.Response

class CreateTaskUseCase(
    private val repository: TaskRepository
) {
    // La firma del método execute ahora acepta la Uri de la imagen
    suspend fun execute(name: String, description: String, id_user: Int, imageUri: Uri?): Response<TaskDTO> {
        return repository.createTask(name, description, id_user, imageUri)
    }
}