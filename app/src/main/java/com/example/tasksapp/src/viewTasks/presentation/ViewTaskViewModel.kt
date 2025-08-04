package com.example.tasksapp.src.viewTasks.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.src.viewTasks.data.model.ViewTaskDTO
import com.example.tasksapp.src.viewTasks.domain.DeleteTaskUseCase
import com.example.tasksapp.src.viewTasks.domain.UpdateTaskUseCase
import com.example.tasksapp.src.viewTasks.domain.ViewTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class ViewTaskViewModel(
    private val viewTaskUseCase: ViewTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase
) : ViewModel()  {

    private val _isEmpty = MutableStateFlow(false)
    val isEmpty: StateFlow<Boolean> = _isEmpty

    private val _tasks = MutableStateFlow<List<ViewTaskDTO>>(emptyList())
    val tasks: StateFlow<List<ViewTaskDTO>> = _tasks

    private val _showEditDialog = MutableStateFlow(false)
    val showEditDialog: StateFlow<Boolean> = _showEditDialog

    private val _taskToEdit = MutableStateFlow<ViewTaskDTO?>(null)
    val taskToEdit: StateFlow<ViewTaskDTO?> = _taskToEdit

    fun loadTasks() {
        viewModelScope.launch {
            try {
                val result = viewTaskUseCase.execute()
                _tasks.value = result
                _isEmpty.value = result.isEmpty()
            } catch (e: Exception) {
                println("Error al cargar tareas: ${e.message}")
            }
        }
    }

    fun deleteTask(id: Int) {
        println("--- PASO 2 (ViewModel): Recibida orden para eliminar ID: $id")
        viewModelScope.launch {
            try {
                val response = deleteTaskUseCase.execute(id)
                if (response.isSuccessful) {
                    loadTasks()
                } else {
                    println("Error al eliminar la tarea: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                println("Error en la solicitud de eliminación: ${e.message}")
            }
        }
    }

    fun onEditClicked(task: ViewTaskDTO) {
        _taskToEdit.value = task
        _showEditDialog.value = true
    }

    fun onDialogDismiss() {
        _showEditDialog.value = false
        _taskToEdit.value = null
    }

    fun updateTask(name: String, description: String) {
        val task = _taskToEdit.value ?: return
        viewModelScope.launch {
            try {
                val response = updateTaskUseCase.execute(task.id, name, description)
                if (response.isSuccessful) {
                    onDialogDismiss()
                    loadTasks()
                } else {
                    println("Error al actualizar la tarea: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                println("Error en la solicitud de actualización: ${e.message}")
            }
        }
    }
}