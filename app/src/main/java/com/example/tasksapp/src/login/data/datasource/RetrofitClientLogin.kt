package com.example.tasksapp.src.login.data.datasource

import com.example.tasksapp.core.petitions.RetrofitClient


object RetrofitClientLogin {

    val api: LoginService by lazy {
        RetrofitClient.instance.create(LoginService::class.java)
    }
}