package com.example.tasksapp.src.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksapp.src.login.domain.LoginUseCase
import com.example.tasksapp.core.dataStore.AuthManager
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf

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
                        AuthManager.setUserId(loginData.id)
                        AuthManager.setToken(loginData.token)
                        println("Logeo Exitoso: ${loginData}")
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

    fun resetLoginState() {
        loginSuccess.value = null
    }
}
