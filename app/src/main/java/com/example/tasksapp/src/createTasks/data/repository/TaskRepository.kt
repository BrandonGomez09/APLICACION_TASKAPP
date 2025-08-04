package com.example.tasksapp.src.createTasks.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.example.tasksapp.src.createTasks.data.datasource.CreateTaskService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class TaskRepository(
    private val api: CreateTaskService,
    private val context: Context
) {
    suspend fun createTask(name: String, description: String, id_user: Int, imageUri: Uri?) =
        api.createTask(
            name = name.toRequestBody("text/plain".toMediaTypeOrNull()),
            description = description.toRequestBody("text/plain".toMediaTypeOrNull()),
            id_user = id_user.toString().toRequestBody("text/plain".toMediaTypeOrNull()),

            // Esta parte no cambia, sigue usando la función auxiliar
            image = imageUri?.let { uri ->
                getFileFromUri(uri)?.let { file ->
                    MultipartBody.Part.createFormData(
                        "image",
                        file.name,
                        file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    )
                }
            }
        )

    // --- FUNCIÓN TOTALMENTE ACTUALIZADA CON LÓGICA DE COMPRESIÓN Y REDIMENSIÓN ---
    private fun getFileFromUri(uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)

            // 1. Decodificar la imagen a un Bitmap, que es un mapa de píxeles manipulable
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            // 2. Redimensionar el Bitmap si es muy grande (ej. más de 1080p)
            val maxHeight = 1080.0
            val maxWidth = 1080.0
            val scale = minOf(maxWidth / originalBitmap.width, maxHeight / originalBitmap.height)
            val scaledBitmap = if (scale < 1) {
                Bitmap.createScaledBitmap(originalBitmap, (originalBitmap.width * scale).toInt(), (originalBitmap.height * scale).toInt(), true)
            } else {
                originalBitmap
            }

            // 3. Crear un nuevo archivo temporal donde guardaremos la imagen comprimida
            val file = File(context.cacheDir, "compressed_image_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)

            // 4. Comprimir el bitmap (ya redimensionado) a formato JPEG con 85% de calidad
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
            outputStream.close()

            // (Opcional) Imprimimos en Logcat la diferencia de tamaño para ver el resultado
            println("Imagen comprimida. Tamaño original: ${originalBitmap.byteCount / 1024} KB. Nuevo tamaño: ${file.length() / 1024} KB")

            // 5. Devolvemos el archivo nuevo, pequeño y comprimido
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}