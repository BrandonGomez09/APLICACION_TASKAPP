// En el archivo: app/src/main/java/com/example/tasksapp/core/services/FCMService.kt

package com.example.tasksapp.core.services

import com.example.tasksapp.core.petitions.RetrofitClient // Reutilizamos tu RetrofitClient principal
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

// Esto define el cuerpo JSON que espera tu API: { "fcm_token": "..." }
data class FcmTokenRequest(val fcm_token: String)

// Interfaz que define el endpoint de la API
interface FCMService {
    @PUT("user/{id}/fcm-token")
    suspend fun updateFcmToken(
        @Path("id") userId: Int,
        @Body request: FcmTokenRequest
    ): Response<Unit>
}

// Objeto para acceder fácilmente al servicio
object FCMApi {
    val service: FCMService by lazy {
        RetrofitClient.instance.create(FCMService::class.java)
    }
}