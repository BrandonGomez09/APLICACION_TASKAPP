package com.example.tasksapp.src.createTasks.data.datasource

import com.example.tasksapp.src.createTasks.data.model.TaskDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CreateTaskService {
    // La anotación @POST se queda, pero ahora la función es @Multipart
    @Multipart
    @POST("task")
    suspend fun createTask(
        // El archivo de imagen. 'image' debe coincidir con el nombre
        // que espera el middleware de multer en tu API: upload.single('image')
        @Part image: MultipartBody.Part?,

        // Los otros campos se envían como RequestBody de texto
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("id_user") id_user: RequestBody
    ): Response<TaskDTO>
}