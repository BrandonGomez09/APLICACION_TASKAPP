package com.example.tasksapp.src.vibration.presentation // <-- ESTA LÍNEA ES CRÍTICA

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun VibrationScreen(vibrationViewModel: VibrationViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Presiona el botón para vibrar el dispositivo")

        Button(
            onClick = { vibrationViewModel.vibrateDevice() },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Vibrar")
        }
    }
}