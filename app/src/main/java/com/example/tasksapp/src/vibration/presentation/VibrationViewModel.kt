package com.example.tasksapp.src.vibration.presentation

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel

class VibrationViewModel(application: Application) : AndroidViewModel(application) {

    fun vibrateDevice(duration: Long = 500) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Para Android 12 (API 31) y superior, usamos VibratorManager
            val vibratorManager = getApplication<Application>().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            // Para versiones anteriores, usamos el método deprecado
            @Suppress("DEPRECATION")
            getApplication<Application>().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        // Una vez que tenemos el 'vibrator', comprobamos de nuevo la versión para usar el método correcto
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Para Android 8 (API 26) y superior, VibrationEffect está disponible
            if (vibrator.hasVibrator()) { // Buena práctica: verificar si el hardware de vibración existe
                val vibrationEffect = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(vibrationEffect)
            }
        } else {
            // Para versiones antiguas (antes de API 26)
            @Suppress("DEPRECATION")
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(duration)
            }
        }
    }
}