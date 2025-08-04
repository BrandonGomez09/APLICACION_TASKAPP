package com.example.tasksapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.tasksapp.core.navigation.AppNavigation
import com.example.tasksapp.src.createTasks.data.datasource.RetrofitClientTask
import com.example.tasksapp.src.createTasks.data.repository.TaskRepository
import com.example.tasksapp.src.createTasks.domain.CreateTaskUseCase
import com.example.tasksapp.src.createTasks.presentation.TasksViewModel
import com.example.tasksapp.src.login.data.datasource.RetrofitClientLogin
import com.example.tasksapp.src.login.data.repository.LoginRepository
import com.example.tasksapp.src.login.domain.LoginUseCase
import com.example.tasksapp.src.login.presentation.LoginViewModel
import com.example.tasksapp.src.register.data.datasource.RetrofitClient
import com.example.tasksapp.src.register.data.repository.RegisterRepository
import com.example.tasksapp.src.register.domain.CreateUserUseCase
import com.example.tasksapp.src.register.presentation.RegisterViewModel
import com.example.tasksapp.src.viewTasks.data.datasource.RetrofitClientViewTask
import com.example.tasksapp.src.viewTasks.data.repository.ViewTaskRepository
import com.example.tasksapp.src.viewTasks.domain.DeleteTaskUseCase
import com.example.tasksapp.src.viewTasks.domain.UpdateTaskUseCase
import com.example.tasksapp.src.viewTasks.domain.ViewTaskUseCase
import com.example.tasksapp.src.viewTasks.presentation.ViewTaskViewModel
import com.example.tasksapp.ui.theme.TaksappTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaksappTheme {
                val navController = rememberNavController()

                // Obtenemos el contexto de la aplicación
                val appContext = applicationContext

                val registerViewModel = RegisterViewModel(CreateUserUseCase(RegisterRepository(RetrofitClient.api)))
                val loginViewModel = LoginViewModel(LoginUseCase(LoginRepository(RetrofitClientLogin.api)))

                // --- LÍNEAS MODIFICADAS ---
                // 1. Creamos el Repositorio pasándole el contexto
                val taskRepository = TaskRepository(RetrofitClientTask.api, appContext)
                // 2. Creamos el Caso de Uso con el nuevo repositorio
                val createTaskUseCase = CreateTaskUseCase(taskRepository)
                // 3. Creamos el ViewModel con el nuevo caso de uso
                val createTasksViewModel = TasksViewModel(createTaskUseCase)
                // --- FIN DE LÍNEAS MODIFICADAS ---


                val viewTaskRepository = ViewTaskRepository(RetrofitClientViewTask.api)
                val viewTaskUseCase = ViewTaskUseCase(viewTaskRepository)
                val deleteTaskUseCase = DeleteTaskUseCase(viewTaskRepository)
                val updateTaskUseCase = UpdateTaskUseCase(viewTaskRepository)
                val viewTasksViewModel = ViewTaskViewModel(
                    viewTaskUseCase,
                    deleteTaskUseCase,
                    updateTaskUseCase
                )

                AppNavigation(
                    navController = navController,
                    loginViewModel = loginViewModel,
                    registerViewModel = registerViewModel,
                    createTasksViewModel = createTasksViewModel,
                    viewTasksViewModel = viewTasksViewModel
                )
            }
        }
    }
}