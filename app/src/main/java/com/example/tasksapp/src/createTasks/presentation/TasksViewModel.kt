package com.example.tasksapp.src.createTasks.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.core.dataStore.AuthManager
import com.example.tasksapp.src.createTasks.domain.CreateTaskUseCase
import kotlinx.coroutines.launch

open class TasksViewModel(
    private val createTaskUseCase: CreateTaskUseCase,
) : ViewModel() {

    // La firma de la función ahora acepta el Uri de la imagen
    fun createTask(name: String, description: String, imageUri: Uri?) {
        viewModelScope.launch {
            try {
                val id_user = AuthManager.getUserId() ?: throw Exception("Usuario no autenticado")

                // Llamamos al caso de uso con todos los parámetros, incluida la imagen
                val response = createTaskUseCase.execute(name, description, id_user, imageUri)

                if (response.isSuccessful) {
                    // El cuerpo de la respuesta ahora debería incluir la imageUrl de la API
                    println("✅ Tarea creada con éxito: ${response.body()}")
                } else {
                    println("❌ Error al crear la tarea: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                // Este error puede saltar si hay problemas con el archivo o la red
                println("⚠️ Error fatal en la solicitud: ${e.message}")
            }
        }
    }
}