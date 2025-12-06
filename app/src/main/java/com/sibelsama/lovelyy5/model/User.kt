package com.sibelsama.lovelyy5.model

import kotlinx.serialization.Serializable
import com.sibelsama.lovelyy5.utils.RutValidator

@Serializable
data class User(
    val rut: String = "", // RUT sin puntos ni guión: "209543842"
    val nombres: String = "", // Nombres: "Jacqueline Sibel"
    val apellidos: String = "", // Apellidos: "Torti Parraguez"
    val email: String = "", // Email
    val passwd: String = "", // Contraseña sin hash
    val telefono: Long = 0, // Teléfono como número: 989598097
    val rol: String = "cliente", // Rol: "cliente", "trabajador", "admin"
    val fotoPerfil: String = "", // URL de foto de perfil
    val createdAt: String = "", // Timestamp de creación
    val direccion: Direccion = Direccion()
) {
    val displayName: String get() = "$nombres $apellidos".trim()
    val formattedRut: String get() = RutValidator.formatRut(rut)
    val formattedTelefono: String get() = if (telefono > 0) "+56$telefono" else ""
    val isAdmin: Boolean get() = rol == "admin"
    val isTrabajador: Boolean get() = rol == "trabajador"
    val isCliente: Boolean get() = rol == "cliente"
}

@Serializable
data class Direccion(
    val calle: String = "",
    val numero: Int = 0,
    val comuna: String = "",
    val region: String = ""
) {
    val direccionCompleta: String get() = "$calle $numero, $comuna, $region".trim()
}

@Serializable
data class UserPreferences(
    val notifications: Boolean = true,
    val emailMarketing: Boolean = false,
    val theme: String = "system", // "light", "dark", "system"
    val language: String = "es",
    val currency: String = "CLP"
)

@Serializable
data class AuthResult(
    val isSuccess: Boolean,
    val user: User? = null,
    val errorMessage: String? = null
)

@Serializable
data class RegisterRequest(
    val rut: String,
    val nombres: String,
    val apellidos: String,
    val email: String,
    val passwd: String,
    val telefono: String? = null,
    val direccion: Direccion = Direccion()
)

@Serializable
data class LoginRequest(
    val identifier: String, // Puede ser RUT o email
    val passwd: String
)
