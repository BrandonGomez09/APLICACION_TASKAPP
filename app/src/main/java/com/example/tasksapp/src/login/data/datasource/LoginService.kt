package com.example.tasksapp.src.login.data.datasource

import com.example.tasksapp.src.login.data.model.LoginDTO
import com.example.tasksapp.src.login.data.model.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("auth/login-admin")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginDTO>
}