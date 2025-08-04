package com.example.tasksapp.src.register.data.datasource

import com.example.tasksapp.core.petitions.RetrofitClient

object RetrofitClient {

    val api: RegisterService by lazy {
        RetrofitClient.instance.create(RegisterService::class.java)
    }
}
