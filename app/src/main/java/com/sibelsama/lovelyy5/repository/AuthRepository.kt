package com.sibelsama.lovelyy5.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sibelsama.lovelyy5.data.AuthService
import com.sibelsama.lovelyy5.model.User
import com.sibelsama.lovelyy5.model.AuthResult
import com.sibelsama.lovelyy5.model.RegisterRequest
import com.sibelsama.lovelyy5.model.LoginRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

class AuthRepository(private val context: Context) {
    private val authService = AuthService()

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")
        private val USER_RUT_KEY = stringPreferencesKey("user_rut")
        private val USER_DATA_KEY = stringPreferencesKey("user_data")
        private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
    }

    /**
     * Flow que emite true si el usuario está autenticado
     */
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN_KEY] ?: false
    }

    /**
     * Flow que emite los datos del usuario actual
     */
    val currentUser: Flow<User?> = context.dataStore.data.map { preferences ->
        val userData = preferences[USER_DATA_KEY]
        if (userData != null) {
            try {
                Json.decodeFromString<User>(userData)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    /**
     * Registrar nuevo usuario
     */
    suspend fun register(request: RegisterRequest): AuthResult {
        val result = authService.registerUser(request)

        if (result.isSuccess && result.user != null) {
            saveUserSession(result.user)
        }

        return result
    }

    /**
     * Iniciar sesión
     */
    suspend fun login(request: LoginRequest): AuthResult {
        val result = authService.loginUser(request)

        if (result.isSuccess && result.user != null) {
            saveUserSession(result.user)
        }

        return result
    }

    /**
     * Cerrar sesión
     */
    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    /**
     * Obtener usuario actual desde cache o servidor
     */
    suspend fun getCurrentUser(): User? {
        // Primero intentar desde cache
        val cachedUser = currentUser.first()
        if (cachedUser != null) {
            return cachedUser
        }

        // Si no hay cache, intentar desde servidor usando el RUT guardado
        val userRut = getUserRut()
        if (userRut != null) {
            val user = authService.getUserByRut(userRut)
            if (user != null) {
                saveUserSession(user)
                return user
            }
        }

        return null
    }

    /**
     * Actualizar perfil del usuario
     */
    suspend fun updateProfile(user: User): Result<User> {
        val result = authService.updateUserProfile(user)

        if (result.isSuccess) {
            saveUserSession(result.getOrNull()!!)
        }

        return result
    }

    /**
     * Cambiar contraseña
     */
    suspend fun changePassword(currentPassword: String, newPassword: String): AuthResult {
        val userRut = getUserRut()
        if (userRut == null) {
            return AuthResult(false, errorMessage = "Usuario no autenticado")
        }

        return authService.changePassword(userRut, currentPassword, newPassword)
    }

    /**
     * Verificar si el usuario está autenticado
     */
    suspend fun isUserAuthenticated(): Boolean {
        return isLoggedIn.first()
    }

    /**
     * Obtener RUT del usuario actual
     */
    private suspend fun getUserRut(): String? {
        return context.dataStore.data.first()[USER_RUT_KEY]
    }

    /**
     * Guardar sesión del usuario
     */
    private suspend fun saveUserSession(user: User) {
        context.dataStore.edit { preferences ->
            preferences[USER_RUT_KEY] = user.rut
            preferences[USER_DATA_KEY] = Json.encodeToString(user)
            preferences[IS_LOGGED_IN_KEY] = true
        }
    }

    /**
     * Refrescar datos del usuario desde el servidor
     */
    suspend fun refreshUser(): User? {
        val userRut = getUserRut()
        if (userRut != null) {
            val user = authService.getUserByRut(userRut)
            if (user != null) {
                saveUserSession(user)
                return user
            }
        }
        return null
    }
}
