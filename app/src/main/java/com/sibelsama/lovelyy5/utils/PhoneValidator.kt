package com.sibelsama.lovelyy5.utils

/**
 * Utilidad para validación y formato de números telefónicos chilenos
 */
object PhoneValidator {

    /**
     * Valida un número telefónico chileno de 9 dígitos
     * @param phone Número telefónico
     * @return true si el número es válido
     */
    fun isValidChileanPhone(phone: String): Boolean {
        val cleanPhone = cleanPhone(phone)

        // Debe tener exactamente 9 dígitos
        if (cleanPhone.length != 9) return false

        // Todos deben ser dígitos
        if (!cleanPhone.all { it.isDigit() }) return false

        // Debe empezar con 9 (celular) o números válidos de fijo
        val firstDigit = cleanPhone.first()
        return firstDigit in listOf('9', '2', '3', '4', '5', '6', '7', '8')
    }

    /**
     * Formatea un número telefónico chileno al formato estándar
     * @param phone Número sin formato
     * @return Número formateado como "+569XXXXXXXX" o "+56XXXXXXXX"
     */
    fun formatChileanPhone(phone: String): String {
        val cleanPhone = cleanPhone(phone)

        if (cleanPhone.length != 9) return phone

        return "+56$cleanPhone"
    }

    /**
     * Formatea para mostrar con espacios
     * Ejemplo: "912345678" -> "+56 9 1234 5678"
     */
    fun formatForDisplay(phone: String): String {
        val cleanPhone = cleanPhone(phone)

        if (cleanPhone.length != 9) return phone

        val firstDigit = cleanPhone.first()
        val restDigits = cleanPhone.drop(1)

        return "+56 $firstDigit ${restDigits.substring(0, 4)} ${restDigits.substring(4)}"
    }

    /**
     * Limpia un número telefónico removiendo espacios, guiones y símbolos
     */
    private fun cleanPhone(phone: String): String {
        return phone.replace(Regex("[^0-9]"), "")
    }

    /**
     * Genera un número telefónico válido para pruebas
     */
    fun generateSamplePhone(): String {
        val firstDigit = listOf('9', '2', '3', '4', '5', '6', '7', '8').random()
        val remainingDigits = (10000000..99999999).random().toString()
        return "$firstDigit$remainingDigits"
    }

    /**
     * Lista de números telefónicos válidos para pruebas (9 dígitos)
     */
    val sampleValidPhones = listOf(
        "912345678", // Celular
        "987654321", // Celular
        "956788901", // Celular
        "923456789", // Celular
        "234567890", // Fijo Santiago
        "322334455", // Fijo Valparaíso
        "412233445", // Fijo Concepción
        "552334567"  // Fijo Norte
    )

    /**
     * Verifica si es un número celular (empieza con 9)
     */
    fun isCellPhone(phone: String): Boolean {
        val cleanPhone = cleanPhone(phone)
        return cleanPhone.length == 9 && cleanPhone.startsWith("9")
    }

    /**
     * Verifica si es un número fijo
     */
    fun isLandline(phone: String): Boolean {
        val cleanPhone = cleanPhone(phone)
        return cleanPhone.length == 9 && !cleanPhone.startsWith("9")
    }
}
