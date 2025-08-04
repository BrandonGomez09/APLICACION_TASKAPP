package com.example.tasksapp.src.viewTasks.domain

import com.example.tasksapp.src.viewTasks.data.repository.ViewTaskRepository
import retrofit2.Response

class DeleteTaskUseCase(private val repository: ViewTaskRepository) {
    suspend fun execute(id: Int): Response<Unit> {
        return repository.deleteTask(id)
    }
}