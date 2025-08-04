// En el archivo: app/src/main/java/com/example/tasksapp/core/services/MyFirebaseMessagingService.kt

package com.example.tasksapp.core.services // Asegúrate que el paquete sea el correcto

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * Esto se ejecuta cuando FCM genera o actualiza un token para el dispositivo.
     * El token es como la "dirección" a la que se envían las notificaciones.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Por ahora, solo imprimimos el token para saber que la app se está comunicando con Firebase.
        Log.d("FCM_TOKEN", "Nuevo token generado: $token")
    }

    /**
     * Esto se ejecuta cuando se recibe un mensaje mientras la app está abierta (en primer plano).
     * Si la app está cerrada o en segundo plano, Android muestra la notificación automáticamente.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("FCM_MESSAGE", "Mensaje recibido de: ${remoteMessage.from}")

        // Verificamos si la notificación tiene datos y los imprimimos.
        remoteMessage.notification?.let {
            val title = it.title
            val body = it.body
            Log.d("FCM_MESSAGE", "Notificación recibida: Título='$title', Cuerpo='$body'")
            // Más adelante, aquí podríamos mostrar una alerta o actualizar la UI.
        }
            }
        }