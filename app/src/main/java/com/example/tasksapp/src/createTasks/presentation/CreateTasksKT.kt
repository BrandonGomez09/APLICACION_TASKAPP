package com.example.tasksapp.src.createTasks.presentation

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt // <-- Esta línea es la clave
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import java.io.File
import java.util.Objects

// ... (El resto del archivo es el mismo que te pasé en la respuesta anterior)
// Puedes dejar el resto de tu archivo como está si ya lo habías pegado. Lo importante
// es que después de sincronizar Gradle, esta línea de import ya no dará error.

@Composable
fun CreateTaskUi(viewModel: TasksViewModel, navController: NavController) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                // El `imageUri` state ya tiene el valor correcto.
            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                val uri = createImageUri(context)
                imageUri = uri
                cameraLauncher.launch(uri)
            } else {
                // Manejar el caso de permiso denegado
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Agrega una Tarea", style = MaterialTheme.typography.headlineSmall)
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                )

                Spacer(modifier = Modifier.height(8.dp))

                imageUri?.let {
                    Card(modifier = Modifier.height(200.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(model = it),
                            contentDescription = "Imagen de la tarea",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                OutlinedButton(
                    onClick = {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Cámara", modifier = Modifier.padding(end = 8.dp))
                    Text(text = "Añadir Foto")
                }

                Button(
                    onClick = {
                        if (name.isNotEmpty()) {
                            viewModel.createTask(name, description, imageUri)
                            navController.navigate("view_tasks") {
                                popUpTo("view_tasks") { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Crear Tarea")
                }
                OutlinedButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Cancelar")
                }
            }
        }
    }
}

private fun createImageUri(context: Context): Uri {
    val imageFile = File(
        context.cacheDir,
        "camera_photo_${System.currentTimeMillis()}.jpg"
    )
    return FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "${context.packageName}.provider",
        imageFile
    )
}