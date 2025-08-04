package com.example.tasksapp.src.viewTasks.presentation

// Importamos lo necesario para las imágenes
import androidx.compose.foundation.Image
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.layout.ContentScale
// El resto de imports...
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.tasksapp.core.dataStore.AuthManager
import com.example.tasksapp.src.viewTasks.data.model.ViewTaskDTO

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewTaskUi(viewModel: ViewTaskViewModel, navController: NavController) {
    fun onLogout(navController: NavController) {
        AuthManager.clearToken()
        navController.navigate("login") { popUpTo(navController.graph.startDestinationId) { inclusive = true } }
    }

    val tasks by viewModel.tasks.collectAsState()
    val isEmpty by viewModel.isEmpty.collectAsState()
    val showDialog by viewModel.showEditDialog.collectAsState()
    val taskToEdit by viewModel.taskToEdit.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadTasks()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (showDialog) {
        taskToEdit?.let { task -> EditTaskDialog(task, { viewModel.onDialogDismiss() }) { n, d -> viewModel.updateTask(n, d) } }
    }

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                title = { Text("Mis Libros") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                actions = {
                    IconButton(onClick = { navController.navigate("vibration") }) {
                        Icon(imageVector = Icons.Default.Warning, contentDescription = "Test Vibration", tint = MaterialTheme.colorScheme.secondary)
                    }
                    IconButton(onClick = { onLogout(navController) }) {
                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Cerrar sesión", tint = MaterialTheme.colorScheme.secondary)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("create_task") },
                shape = RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Crear tarea", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (isEmpty) {
                Text("No tienes tareas pendientes.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.secondary, modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(tasks, key = { it.id }) { task ->
                        TasksCard(task, viewModel)
                    }
                }
            }
        }
    }
}

// --- TasksCard MODIFICADO ---
@Composable
fun TasksCard(task: ViewTaskDTO, viewModel: ViewTaskViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
    ) {
        Column { // Se envuelve todo en una columna para apilar la imagen y el texto
            // Si la tarea tiene una URL de imagen, la mostramos
            task.imageUrl?.let { url ->
                Image(
                    painter = rememberAsyncImagePainter(model = url), // Coil carga la imagen desde la URL
                    contentDescription = "Imagen de: ${task.name}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp), // Altura fija para la imagen
                    contentScale = ContentScale.Crop // La imagen cubre el área sin deformarse
                )
            }

            // El contenido de texto y botones que ya tenías
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = task.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = task.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                }
                IconButton(onClick = { viewModel.onEditClicked(task) }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar Tarea", tint = MaterialTheme.colorScheme.secondary)
                }
                IconButton(onClick = { viewModel.deleteTask(task.id) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar Tarea", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

// --- EditTaskDialog MODIFICADO ---
@Composable
fun EditTaskDialog(task: ViewTaskDTO, onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var name by remember { mutableStateOf(task.name) }
    var description by remember { mutableStateOf(task.description) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Tarea") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Se añade la imagen al diálogo de edición
                task.imageUrl?.let { url ->
                    Card(modifier = Modifier.height(150.dp).fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(model = url),
                            contentDescription = "Imagen actual",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nuevo nombre") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Nueva descripción") })
            }
        },
        confirmButton = { Button(onClick = { onConfirm(name, description) }) { Text("Guardar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}