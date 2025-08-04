package com.example.tasksapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tasksapp.src.createTasks.presentation.CreateTaskUi
import com.example.tasksapp.src.createTasks.presentation.TasksViewModel
import com.example.tasksapp.src.login.presentation.LoginUi
import com.example.tasksapp.src.login.presentation.LoginViewModel
import com.example.tasksapp.src.register.presentation.RegisterUi
import com.example.tasksapp.src.register.presentation.RegisterViewModel
import com.example.tasksapp.src.viewTasks.presentation.ViewTaskUi
import com.example.tasksapp.src.viewTasks.presentation.ViewTaskViewModel
import com.example.tasksapp.src.vibration.presentation.VibrationScreen // <-- LÍNEA AÑADIDA

@Composable
fun AppNavigation(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
    createTasksViewModel: TasksViewModel,
    viewTasksViewModel: ViewTaskViewModel
) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginUi(viewModel = loginViewModel, navController)
        }
        composable("register") {
            RegisterUi(viewModel = registerViewModel, navController)
        }
        composable("create_task") {
            CreateTaskUi(viewModel = createTasksViewModel, navController)
        }
        composable("view_tasks") {
            ViewTaskUi(viewModel = viewTasksViewModel, navController)
        }
        composable("vibration") {
            VibrationScreen()
        }
    }
}