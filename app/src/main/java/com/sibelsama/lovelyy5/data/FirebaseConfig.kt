package com.sibelsama.lovelyy5.data

import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

/**
 * Configuración y inicialización de Firebase
 * Esta clase debe ser inicializada en el Application o MainActivity
 */
object FirebaseConfig {

    private var isInitialized = false

    /**
     * Inicializa Firebase con configuración optimizada
     * Llamar esto en Application.onCreate() o MainActivity.onCreate()
     */
    fun initialize() {
        if (isInitialized) {
            android.util.Log.d("FirebaseConfig", "Firebase already initialized")
            return
        }

        try {
            // Firebase se inicializa automáticamente con google-services.json
            // pero podemos configurar opciones adicionales

            val firestore = FirebaseFirestore.getInstance()

            // Configuración de Firestore con APIs modernas
            val settings = FirebaseFirestoreSettings.Builder()
                .build()

            firestore.firestoreSettings = settings

            isInitialized = true
            android.util.Log.d("FirebaseConfig", "Firebase initialized successfully")

        } catch (e: Exception) {
            android.util.Log.e("FirebaseConfig", "Error initializing Firebase", e)
            // No lanzar excepción para permitir que la app funcione sin Firebase
        }
    }

    /**
     * Verifica si Firebase está disponible
     */
    fun isAvailable(): Boolean {
        return try {
            FirebaseApp.getInstance()
            true
        } catch (e: IllegalStateException) {
            false
        }
    }

    /**
     * Habilita el modo offline de Firestore
     */
    fun enableOfflineMode() {
        try {
            val firestore = FirebaseFirestore.getInstance()
            firestore.disableNetwork()
            android.util.Log.d("FirebaseConfig", "Offline mode enabled")
        } catch (e: Exception) {
            android.util.Log.e("FirebaseConfig", "Error enabling offline mode", e)
        }
    }

    /**
     * Deshabilita el modo offline de Firestore
     */
    fun disableOfflineMode() {
        try {
            val firestore = FirebaseFirestore.getInstance()
            firestore.enableNetwork()
            android.util.Log.d("FirebaseConfig", "Offline mode disabled")
        } catch (e: Exception) {
            android.util.Log.e("FirebaseConfig", "Error disabling offline mode", e)
        }
    }
}

