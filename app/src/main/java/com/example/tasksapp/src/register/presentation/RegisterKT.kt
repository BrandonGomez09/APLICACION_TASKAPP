package com.example.tasksapp.src.register.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RegisterUi(viewModel: RegisterViewModel, navController: NavController) {
    val name by viewModel.name.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val registrationResult by viewModel.registrationResult.observeAsState()
    var isPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(registrationResult) {
        when (registrationResult) {
            is RegistrationResult.Success -> {
                navController.navigate("login"){ popUpTo("register") { inclusive = true } }
                viewModel.resetRegistrationResult()
                viewModel.resetRegistrationForm()
            }
            is RegistrationResult.Error -> { /* Manejar error si se desea */ }
            null -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background), // <-- CAMBIO
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
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Logo",
                    tint = MaterialTheme.colorScheme.primary, // <-- CAMBIO
                    modifier = Modifier.size(72.dp)
                )
                Text(text = "Registrarse", style = MaterialTheme.typography.headlineSmall)
                TextField(
                    value = name,
                    onValueChange = { viewModel.onNameChanged(it) },
                    label = { Text("Usuario") },
                    leadingIcon = { Icon(Icons.Default.Person, "Usuario") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                )
                TextField(
                    value = password,
                    onValueChange = { viewModel.onPasswordChanged(it) },
                    label = { Text("Contraseña") },
                    leadingIcon = { Icon(Icons.Default.Lock, "Contraseña") },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(if (isPasswordVisible) Icons.Default.Close else Icons.Default.Check, "Mostrar/Ocultar contraseña")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                )
                Button(
                    onClick = { viewModel.registerUser() },
                    enabled = name.isNotEmpty() && password.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                    // <-- CAMBIO: Sin `colors`
                ) {
                    Text(text = "Registrarse")
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