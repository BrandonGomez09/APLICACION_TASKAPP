package com.example.tasksapp.src.viewTasks.data.datasource

import com.example.tasksapp.src.viewTasks.data.model.ViewTaskDTO
import com.example.tasksapp.src.viewTasks.data.model.ViewTaskRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ViewTaskService {
    @GET("task/{id_user}")
    suspend fun viewTasks(@Path("id_user") idUser: Int): List<ViewTaskDTO>

    @DELETE("task/{idtasks}")
    suspend fun deleteTask(@Path("idtasks") id: Int): Response<Unit>

    @PUT("task/{idtasks}")
    suspend fun updateTask(
        @Path("idtasks") id: Int,
        @Body request: ViewTaskRequest
    ): Response<Unit>
}