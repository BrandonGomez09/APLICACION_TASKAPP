package com.example.tasksapp.src.register.data.repository

import com.example.tasksapp.src.register.data.datasource.RegisterService
import com.example.tasksapp.src.register.data.model.CreateUserRequest

class RegisterRepository(private val api: RegisterService) {
    suspend fun registerUser(name: String, password: String) =
        api.registerUser(CreateUserRequest(name,  password))
}