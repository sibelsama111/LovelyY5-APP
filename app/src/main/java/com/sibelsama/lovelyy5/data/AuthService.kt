package com.sibelsama.lovelyy5.data

import com.google.firebase.firestore.FirebaseFirestore
import com.sibelsama.lovelyy5.model.User
import com.sibelsama.lovelyy5.model.AuthResult
import com.sibelsama.lovelyy5.model.RegisterRequest
import com.sibelsama.lovelyy5.model.LoginRequest
import com.sibelsama.lovelyy5.utils.RutValidator
import kotlinx.coroutines.tasks.await
import java.time.Instant

class AuthService {
    private val db = FirebaseFirestore.getInstance()

    /**
     * Registrar nuevo usuario (solo clientes)
     */
    suspend fun registerUser(request: RegisterRequest): AuthResult {
        return try {
            // Verificar que el RUT no exista
            val existingUser = findUserByRut(request.rut)
            if (existingUser != null) {
                return AuthResult(
                    isSuccess = false,
                    errorMessage = "El RUT ya está registrado"
                )
            }

            // Verificar que el email no exista
            val existingEmailUser = findUserByEmail(request.email)
            if (existingEmailUser != null) {
                return AuthResult(
                    isSuccess = false,
                    errorMessage = "El email ya está registrado"
                )
            }

            // Validar datos
            val validation = validateRegisterData(request)
            if (!validation.isSuccess) {
                return validation
            }

            // Crear nuevo usuario (siempre cliente)
            val storageRut = RutValidator.toStorageFormat(request.rut)
            val currentTime = Instant.now().toString()

            val user = User(
                rut = storageRut,
                nombres = request.nombres.trim(),
                apellidos = request.apellidos.trim(),
                email = request.email.lowercase().trim(),
                passwd = request.passwd, // Guardar contraseña sin hash
                telefono = request.telefono?.toLongOrNull() ?: 0,
                rol = "cliente", // Solo clientes pueden registrarse
                fotoPerfil = "", // Vacío inicialmente
                createdAt = currentTime,
                direccion = request.direccion
            )

            // Guardar en Firestore usando RUT sin guión como ID del documento
            db.collection("users")
                .document(storageRut)
                .set(user)
                .await()

            AuthResult(
                isSuccess = true,
                user = user
            )

        } catch (e: Exception) {
            AuthResult(
                isSuccess = false,
                errorMessage = "Error al registrar usuario: ${e.message}"
            )
        }
    }

    /**
     * Iniciar sesión (con RUT o email)
     */
    suspend fun loginUser(request: LoginRequest): AuthResult {
        return try {
            // Determinar si el identificador es RUT o email
            val user = if (RutValidator.isRutFormat(request.identifier)) {
                findUserByRut(request.identifier)
            } else {
                findUserByEmail(request.identifier)
            }

            if (user == null) {
                return AuthResult(
                    isSuccess = false,
                    errorMessage = "Credenciales incorrectas"
                )
            }

            // Verificar contraseña (comparación directa)
            if (request.passwd != user.passwd) {
                return AuthResult(
                    isSuccess = false,
                    errorMessage = "Credenciales incorrectas"
                )
            }

            AuthResult(
                isSuccess = true,
                user = user
            )

        } catch (e: Exception) {
            AuthResult(
                isSuccess = false,
                errorMessage = "Error al iniciar sesión: ${e.message}"
            )
        }
    }


    /**
     * Obtener usuario por RUT (público)
     */
    suspend fun getUserByRut(rut: String): User? {
        return try {
            val storageRut = RutValidator.toStorageFormat(rut)
            val document = db.collection("users")
                .document(storageRut)
                .get()
                .await()

            document.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Obtener usuario por RUT (privado para uso interno)
     */
    private suspend fun findUserByRut(rut: String): User? {
        return getUserByRut(rut)
    }

    /**
     * Obtener usuario por email
     */
    private suspend fun findUserByEmail(email: String): User? {
        return try {
            val querySnapshot = db.collection("users")
                .whereEqualTo("email", email.lowercase().trim())
                .get()
                .await()

            if (querySnapshot.documents.isNotEmpty()) {
                querySnapshot.documents.first().toObject(User::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Actualizar perfil de usuario
     */
    suspend fun updateUserProfile(user: User): Result<User> {
        return try {
            val storageRut = RutValidator.toStorageFormat(user.rut)
            db.collection("users")
                .document(storageRut)
                .set(user)
                .await()

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Cambiar contraseña
     */
    suspend fun changePassword(rut: String, currentPassword: String, newPassword: String): AuthResult {
        return try {
            val user = findUserByRut(rut)
            if (user == null) {
                return AuthResult(
                    isSuccess = false,
                    errorMessage = "Usuario no encontrado"
                )
            }

            // Verificar contraseña actual (comparación directa)
            if (currentPassword != user.passwd) {
                return AuthResult(
                    isSuccess = false,
                    errorMessage = "Contraseña actual incorrecta"
                )
            }

            // Validar nueva contraseña
            if (!isValidPassword(newPassword)) {
                return AuthResult(
                    isSuccess = false,
                    errorMessage = "La contraseña debe tener al menos 6 caracteres"
                )
            }

            // Actualizar contraseña (guardar sin hash)
            val storageRut = RutValidator.toStorageFormat(user.rut)
            db.collection("users")
                .document(storageRut)
                .update("passwd", newPassword)
                .await()

            AuthResult(isSuccess = true)
        } catch (e: Exception) {
            AuthResult(
                isSuccess = false,
                errorMessage = "Error al cambiar contraseña: ${e.message}"
            )
        }
    }

    /**
     * Validar datos de registro
     */
    private fun validateRegisterData(request: RegisterRequest): AuthResult {
        if (request.rut.isBlank()) {
            return AuthResult(false, errorMessage = "El RUT es requerido")
        }

        if (!RutValidator.isValidRut(request.rut)) {
            return AuthResult(false, errorMessage = "RUT inválido")
        }

        if (request.nombres.isBlank()) {
            return AuthResult(false, errorMessage = "Los nombres son requeridos")
        }

        if (request.apellidos.isBlank()) {
            return AuthResult(false, errorMessage = "Los apellidos son requeridos")
        }

        if (request.email.isBlank()) {
            return AuthResult(false, errorMessage = "El email es requerido")
        }

        if (!isValidEmail(request.email)) {
            return AuthResult(false, errorMessage = "Email inválido")
        }

        if (!isValidPassword(request.passwd)) {
            return AuthResult(false, errorMessage = "La contraseña debe tener al menos 6 caracteres")
        }

        // Validar teléfono si se proporciona
        if (!request.telefono.isNullOrBlank()) {
            val phoneNumber = request.telefono.toLongOrNull()
            if (phoneNumber == null || phoneNumber.toString().length != 9) {
                return AuthResult(false, errorMessage = "El teléfono debe tener exactamente 9 dígitos")
            }
        }

        return AuthResult(true)
    }

    /**
     * Validar email
     */
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Validar contraseña
     */
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

// Las contraseñas se guardan directamente sin hash según requerimiento
}
