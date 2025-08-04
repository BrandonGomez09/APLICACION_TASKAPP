package com.example.tasksapp.src.register.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.tasksapp.src.register.domain.CreateUserUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class RegisterViewModel(
    private val createUserUseCase: CreateUserUseCase
) : ViewModel() {

    private val _name = MutableLiveData("")
    val name: LiveData<String> = _name

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    private val _registrationResult = MutableLiveData<RegistrationResult>()
    val registrationResult: LiveData<RegistrationResult> = _registrationResult

    fun onNameChanged(newName: String) {
        _name.value = newName
    }

    fun onPasswordChanged(newPassword: String) {
        _password.value = newPassword
    }

    fun resetRegistrationResult() {
        _registrationResult.value = null
    }

    fun resetRegistrationForm() {
        _name.value = ""
        _password.value = ""
        _registrationResult.value = null
    }

    fun registerUser() {
        viewModelScope.launch {
            val currentName = _name.value ?: ""
            val currentPassword = _password.value ?: ""

            if (currentName.isEmpty() || currentPassword.isEmpty()) {
                _registrationResult.value = RegistrationResult.Error("Missing data")
                return@launch
            }

            try {
                val response = createUserUseCase.execute(currentName, currentPassword)

                if (response.isSuccessful) {
                    _registrationResult.value = RegistrationResult.Success
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    _registrationResult.value = RegistrationResult.Error(errorBody)
                }
            } catch (e: Exception) {
                _registrationResult.value = RegistrationResult.Error("Register error: ${e.message}")
            }
        }
    }
}

sealed class RegistrationResult {
    object Success : RegistrationResult()
    data class Error(val message: String) : RegistrationResult()
}
