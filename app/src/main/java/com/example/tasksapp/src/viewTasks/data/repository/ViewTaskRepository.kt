package com.example.tasksapp.src.viewTasks.data.repository

import com.example.tasksapp.core.dataStore.AuthManager
import com.example.tasksapp.src.viewTasks.data.datasource.ViewTaskService
import com.example.tasksapp.src.viewTasks.data.model.ViewTaskDTO
import com.example.tasksapp.src.viewTasks.data.model.ViewTaskRequest
import retrofit2.Response

class ViewTaskRepository(private val api: ViewTaskService) {
    suspend fun getTasks(): List<ViewTaskDTO> {
        val id_user = AuthManager.getUserId() ?: throw Exception("Usuario no autenticado")
        return api.viewTasks(id_user)
    }

    suspend fun deleteTask(id: Int): Response<Unit> {
        return api.deleteTask(id)
    }

    suspend fun updateTask(id: Int, name: String, description: String): Response<Unit> {
        val request = ViewTaskRequest(name = name, description = description)
        return api.updateTask(id, request)
    }
}