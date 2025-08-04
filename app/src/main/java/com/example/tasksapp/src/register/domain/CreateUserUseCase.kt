package com.example.tasksapp.src.register.domain

import com.example.tasksapp.src.register.data.repository.RegisterRepository
import com.example.tasksapp.src.register.data.model.UserDTO
import retrofit2.Response

class CreateUserUseCase(private val repository: RegisterRepository) {
    suspend fun execute(name: String, password: String): Response<UserDTO> {
        return repository.registerUser(name, password)
    }
}