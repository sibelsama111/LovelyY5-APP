package com.sibelsama.lovelyy5.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sibelsama.lovelyy5.model.User
import com.sibelsama.lovelyy5.model.AuthResult
import com.sibelsama.lovelyy5.model.RegisterRequest
import com.sibelsama.lovelyy5.model.LoginRequest
import com.sibelsama.lovelyy5.model.Direccion
import com.sibelsama.lovelyy5.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application.applicationContext)

    // Estado de la UI
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _authResult = MutableStateFlow<AuthResult?>(null)
    val authResult: StateFlow<AuthResult?> = _authResult.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // Estados del repositorio
    val isLoggedIn = authRepository.isLoggedIn
    val userFlow = authRepository.currentUser

    init {
        // Cargar usuario actual si existe
        viewModelScope.launch {
            _currentUser.value = authRepository.getCurrentUser()
        }

        // Observar cambios del usuario
        viewModelScope.launch {
            userFlow.collect { user ->
                _currentUser.value = user
            }
        }
    }

    /**
     * Registrar nuevo usuario (cliente)
     */
    fun register(
        rut: String,
        nombres: String,
        apellidos: String,
        email: String,
        passwd: String,
        confirmPassword: String,
        telefono: String? = null,
        direccion: Direccion = Direccion()
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _authResult.value = null

            try {
                // Validar que las contraseñas coincidan
                if (passwd != confirmPassword) {
                    _authResult.value = AuthResult(
                        isSuccess = false,
                        errorMessage = "Las contraseñas no coinciden"
                    )
                    return@launch
                }

                val request = RegisterRequest(
                    rut = rut.trim(),
                    nombres = nombres.trim(),
                    apellidos = apellidos.trim(),
                    email = email.trim(),
                    passwd = passwd,
                    telefono = telefono?.trim(),
                    direccion = direccion
                )

                val result = authRepository.register(request)
                _authResult.value = result

                if (result.isSuccess) {
                    _currentUser.value = result.user
                }

            } catch (e: Exception) {
                _authResult.value = AuthResult(
                    isSuccess = false,
                    errorMessage = "Error inesperado: ${e.message}"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Iniciar sesión (con RUT o email)
     */
    fun login(identifier: String, passwd: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _authResult.value = null

            try {
                val request = LoginRequest(
                    identifier = identifier.trim(),
                    passwd = passwd
                )

                val result = authRepository.login(request)
                _authResult.value = result

                if (result.isSuccess) {
                    _currentUser.value = result.user
                }

            } catch (e: Exception) {
                _authResult.value = AuthResult(
                    isSuccess = false,
                    errorMessage = "Error inesperado: ${e.message}"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Cerrar sesión
     */
    fun logout() {
        viewModelScope.launch {
            try {
                authRepository.logout()
                _currentUser.value = null
                _authResult.value = null
            } catch (e: Exception) {
                android.util.Log.e("AuthViewModel", "Error al cerrar sesión", e)
            }
        }
    }

    /**
     * Actualizar perfil del usuario
     */
    fun updateProfile(user: User) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val result = authRepository.updateProfile(user)
                if (result.isSuccess) {
                    _currentUser.value = result.getOrNull()
                    _authResult.value = AuthResult(isSuccess = true)
                } else {
                    _authResult.value = AuthResult(
                        isSuccess = false,
                        errorMessage = "Error al actualizar perfil"
                    )
                }
            } catch (e: Exception) {
                _authResult.value = AuthResult(
                    isSuccess = false,
                    errorMessage = "Error inesperado: ${e.message}"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Cambiar contraseña
     */
    fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _authResult.value = null

            try {
                if (newPassword != confirmPassword) {
                    _authResult.value = AuthResult(
                        isSuccess = false,
                        errorMessage = "Las contraseñas no coinciden"
                    )
                    return@launch
                }

                val result = authRepository.changePassword(currentPassword, newPassword)
                _authResult.value = result

            } catch (e: Exception) {
                _authResult.value = AuthResult(
                    isSuccess = false,
                    errorMessage = "Error inesperado: ${e.message}"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Refrescar datos del usuario
     */
    fun refreshUser() {
        viewModelScope.launch {
            try {
                val user = authRepository.refreshUser()
                _currentUser.value = user
            } catch (e: Exception) {
                android.util.Log.e("AuthViewModel", "Error al refrescar usuario", e)
            }
        }
    }

    /**
     * Limpiar resultado de autenticación
     */
    fun clearAuthResult() {
        _authResult.value = null
    }

    /**
     * Verificar autenticación al iniciar la app
     */
    fun checkAuthenticationStatus() {
        viewModelScope.launch {
            try {
                val isAuthenticated = authRepository.isUserAuthenticated()
                if (isAuthenticated) {
                    val user = authRepository.getCurrentUser()
                    _currentUser.value = user
                }
            } catch (e: Exception) {
                android.util.Log.e("AuthViewModel", "Error verificando autenticación", e)
            }
        }
    }
}
