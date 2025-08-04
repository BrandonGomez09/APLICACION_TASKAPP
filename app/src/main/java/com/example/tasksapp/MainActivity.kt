// En el archivo: app/src/main/java/com/example/tasksapp/MainActivity.kt

package com.example.tasksapp

import android.Manifest // <-- IMPORTACIÓN AÑADIDA
import android.annotation.SuppressLint
import android.os.Build // <-- IMPORTACIÓN AÑADIDA
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts // <-- IMPORTACIÓN AÑADIDA
import androidx.compose.runtime.LaunchedEffect // <-- IMPORTACIÓN AÑADIDA
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
import androidx.activity.compose.rememberLauncherForActivityResult // <-- IMPORTACIÓN AÑADIDA

class MainActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaksappTheme {
                // ================================================================
                //     INICIO DE LA LÓGICA PARA PEDIR PERMISO DE NOTIFICACIONES
                // ================================================================

                // 1. Creamos un "lanzador" que se encargará de pedir el permiso.
                //    No hacemos nada especial con el resultado (si lo acepta o no),
                //    simplemente lo pedimos.
                val requestPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted: Boolean ->
                        if (isGranted) {
                            // El permiso fue concedido. ¡Genial!
                            println("Permiso de notificaciones CONCEDIDO.")
                        } else {
                            // El permiso fue denegado.
                            println("Permiso de notificaciones DENEGADO.")
                        }
                    }
                )

                // 2. Usamos LaunchedEffect para que este código se ejecute solo una vez
                //    cuando la app arranca.
                LaunchedEffect(Unit) {
                    // 3. Verificamos si la versión de Android es 13 (TIRAMISU) o superior.
                    //    En versiones anteriores, no es necesario pedir este permiso.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        // 4. Lanzamos la petición del permiso.
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }

                // ================================================================
                //      FIN DE LA LÓGICA PARA PEDIR PERMISO DE NOTIFICACIONES
                // ================================================================

                val navController = rememberNavController()
                val appContext = applicationContext

                // ... (el resto de tu código de inicialización de ViewModels no cambia)
                val registerViewModel = RegisterViewModel(CreateUserUseCase(RegisterRepository(RetrofitClient.api)))
                val loginViewModel = LoginViewModel(LoginUseCase(LoginRepository(RetrofitClientLogin.api)))
                val taskRepository = TaskRepository(RetrofitClientTask.api, appContext)
                val createTaskUseCase = CreateTaskUseCase(taskRepository)
                val createTasksViewModel = TasksViewModel(createTaskUseCase)
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