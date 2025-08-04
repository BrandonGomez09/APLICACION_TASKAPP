package com.example.tasksapp.src.viewTasks.domain

import com.example.tasksapp.src.viewTasks.data.model.ViewTaskDTO
import com.example.tasksapp.src.viewTasks.data.repository.ViewTaskRepository

class ViewTaskUseCase(private val repository: ViewTaskRepository) {
    suspend fun execute(): List<ViewTaskDTO> {
        return repository.getTasks()
    }
}