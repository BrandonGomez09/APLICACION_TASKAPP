// En el archivo: app/src/main/java/com/example/tasksapp/src/login/presentation/LoginViewModel.kt

package com.example.tasksapp.src.login.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.core.dataStore.AuthManager
import com.example.tasksapp.core.services.FCMApi
import com.example.tasksapp.core.services.FcmTokenRequest
import com.example.tasksapp.src.login.domain.LoginUseCase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

open class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    var loginSuccess = mutableStateOf<Boolean?>(null)
        private set

    fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            try {
                val response = loginUseCase.execute(username, password)
                if (response.isSuccessful) {
                    val loginData = response.body()
                    if (loginData != null) {
                        // Guardamos los datos de sesión como antes
                        AuthManager.setUserId(loginData.id)
                        AuthManager.setToken(loginData.token)
                        println("Logeo Exitoso: ${loginData}")

                        // 1. Enviamos el token individual (esto no cambia)
                        updateFcmTokenForUser(loginData.id)

                        // ===============================================
                        //      2. SUSCRIBIMOS AL TÓPICO GENERAL
                        // ===============================================
                        subscribeToGeneralTopic()
                        // ===============================================

                        loginSuccess.value = true
                    } else {
                        println("Respuesta vacía")
                        loginSuccess.value = false
                    }
                } else {
                    println("Nombre o Contraseña invalido: ${response.errorBody()?.string()}")
                    loginSuccess.value = false
                }
            } catch (e: Exception) {
                println("Error en la solicitud: ${e.message}")
                loginSuccess.value = false
            }
        }
    }

    /**
     *  --- FUNCIÓN NUEVA AÑADIDA ---
     *  Suscribe este dispositivo al tópico "avisos_generales" para recibir notificaciones masivas.
     */
    private fun subscribeToGeneralTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("avisos_generales")
            .addOnCompleteListener { task ->
                var msg = "Suscripción al tópico 'avisos_generales' exitosa."
                if (!task.isSuccessful) {
                    msg = "Falló la suscripción al tópico 'avisos_generales'."
                }
                // Usamos un tag diferente en el Logcat para identificar este proceso
                Log.d("FCM_TOPIC", msg)
            }
    }

    private fun updateFcmTokenForUser(userId: Int) {
        viewModelScope.launch {
            try {
                val fcmToken = FirebaseMessaging.getInstance().token.await()
                Log.d("FCM_TOKEN", "Token obtenido para enviar al backend: $fcmToken")

                val request = FcmTokenRequest(fcm_token = fcmToken)
                val response = FCMApi.service.updateFcmToken(userId, request)

                if (response.isSuccessful) {
                    Log.d("FCM_TOKEN", "¡Éxito! Token FCM actualizado en el backend.")
                } else {
                    Log.e("FCM_TOKEN", "Error al actualizar el token en el backend: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("FCM_TOKEN", "Excepción al obtener o enviar el token FCM", e)
            }
        }
    }

    fun resetLoginState() {
        loginSuccess.value = null
    }
}