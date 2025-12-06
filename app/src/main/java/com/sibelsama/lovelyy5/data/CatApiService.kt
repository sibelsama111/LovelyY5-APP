package com.sibelsama.lovelyy5.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

/**
 * Servicio para obtener imágenes aleatorias de gatos desde TheCatAPI
 */
class CatApiService {

    private val baseUrl = "https://api.thecatapi.com/v1/images/search"

    /**
     * Obtiene una imagen aleatoria de gato
     * @return URL de la imagen o null si hay error
     */
    suspend fun getRandomCatImage(): String? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(baseUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.connectTimeout = 10000
                connection.readTimeout = 10000

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }

                    // Parse simple JSON response
                    // Formato esperado: [{"id":"...","url":"https://...","width":...,"height":...}]
                    val urlPattern = "\"url\"\\s*:\\s*\"([^\"]+)\"".toRegex()
                    val match = urlPattern.find(response)

                    match?.groups?.get(1)?.value
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    /**
     * Verifica si una URL de imagen es válida
     */
    suspend fun isImageUrlValid(imageUrl: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "HEAD"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                connection.responseCode == HttpURLConnection.HTTP_OK
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * URLs de fallback en caso de que la API no funcione
     */
    private val fallbackCatImages = listOf(
        "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg",
        "https://cdn2.thecatapi.com/images/MTY3ODIyMQ.jpg",
        "https://cdn2.thecatapi.com/images/MjA2ODgzNA.jpg",
        "https://cdn2.thecatapi.com/images/bpc.jpg",
        "https://cdn2.thecatapi.com/images/e35.jpg"
    )

    /**
     * Obtiene una imagen de fallback aleatoria
     */
    fun getFallbackCatImage(): String {
        return fallbackCatImages.random()
    }
}
