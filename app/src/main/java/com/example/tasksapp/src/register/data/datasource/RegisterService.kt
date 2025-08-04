package com.example.tasksapp.src.register.data.datasource

import com.example.tasksapp.src.register.data.model.CreateUserRequest
import com.example.tasksapp.src.register.data.model.UserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface RegisterService {
    @POST("user")
    suspend fun registerUser(@Body request: CreateUserRequest): Response<UserDTO>
}