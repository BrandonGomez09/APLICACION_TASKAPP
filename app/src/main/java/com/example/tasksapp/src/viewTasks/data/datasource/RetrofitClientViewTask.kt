package com.example.tasksapp.src.viewTasks.data.datasource


import com.example.tasksapp.core.petitions.RetrofitClient


object RetrofitClientViewTask {

    val api: ViewTaskService by lazy {
        RetrofitClient.instance.create(ViewTaskService::class.java)
    }
}