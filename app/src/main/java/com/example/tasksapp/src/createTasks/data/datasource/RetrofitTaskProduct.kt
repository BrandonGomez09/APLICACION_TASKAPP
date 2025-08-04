package com.example.tasksapp.src.createTasks.data.datasource

import com.example.tasksapp.core.petitions.RetrofitClient

object RetrofitClientTask {

    val api: CreateTaskService by lazy {
        RetrofitClient.instance.create(CreateTaskService::class.java)
    }
}