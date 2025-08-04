package com.example.tasksapp.src.viewTasks.domain

import com.example.tasksapp.src.viewTasks.data.repository.ViewTaskRepository
import retrofit2.Response

class UpdateTaskUseCase(private val repository: ViewTaskRepository) {
    suspend fun execute(id: Int, name: String, description: String): Response<Unit> {
        return repository.updateTask(id, name, description)
    }
}