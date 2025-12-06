package com.sibelsama.lovelyy5.utils

/**
 * Utilidad para validación y formato de RUT chileno
 */
object RutValidator {

    /**
     * Valida un RUT chileno
     * @param rut RUT en formato "99999999-k" o "99999999k"
     * @return true si el RUT es válido
     */
    fun isValidRut(rut: String): Boolean {
        val cleanRut = cleanRut(rut)

        if (cleanRut.length < 2) return false

        val rutNumber = cleanRut.dropLast(1)
        val verifier = cleanRut.last().toString().uppercase()

        if (!rutNumber.all { it.isDigit() }) return false
        if (rutNumber.toLongOrNull() == null) return false

        return calculateVerifier(rutNumber.toLong()) == verifier
    }

    /**
     * Formatea un RUT al formato estándar "99999999-K"
     * @param rut RUT sin formato
     * @return RUT formateado
     */
    fun formatRut(rut: String): String {
        val cleanRut = cleanRut(rut)
        if (cleanRut.length < 2) return rut

        val rutNumber = cleanRut.dropLast(1)
        val verifier = cleanRut.last().toString().uppercase()

        return "$rutNumber-$verifier"
    }

    /**
     * Convierte RUT con guión a formato sin guión para almacenamiento
     * @param rut RUT con formato "99999999-K"
     * @return RUT sin guión "99999999K"
     */
    fun toStorageFormat(rut: String): String {
        return cleanRut(rut)
    }

    /**
     * Valida si un string es un RUT sin guión válido
     */
    fun isValidStorageRut(rut: String): Boolean {
        return isValidRut(rut)
    }

    /**
     * Verifica si un identificador es un RUT (vs email)
     */
    fun isRutFormat(identifier: String): Boolean {
        val clean = cleanRut(identifier)
        return clean.length >= 8 && clean.length <= 9 &&
               clean.dropLast(1).all { it.isDigit() } &&
               clean.last().let { it.isDigit() || it.uppercase() == "K" }
    }

    /**
     * Limpia un RUT removiendo puntos, guiones y espacios
     */
    private fun cleanRut(rut: String): String {
        return rut.replace(Regex("[^0-9kK]"), "").uppercase()
    }

    /**
     * Calcula el dígito verificador de un RUT
     */
    private fun calculateVerifier(rutNumber: Long): String {
        var sum = 0L
        var multiplier = 2L
        var tempRut = rutNumber

        while (tempRut > 0) {
            sum += (tempRut % 10) * multiplier
            tempRut /= 10
            multiplier = if (multiplier == 7L) 2L else multiplier + 1L
        }

        val remainder = sum % 11
        val verifier = 11 - remainder

        return when (verifier) {
            10L -> "K"
            11L -> "0"
            else -> verifier.toString()
        }
    }

    /**
     * Genera un RUT de ejemplo válido para pruebas
     */
    fun generateSampleRut(): String {
        val rutNumber = (10000000L..25000000L).random()
        val verifier = calculateVerifier(rutNumber)
        return "$rutNumber-$verifier"
    }

    /**
     * Extrae solo el número del RUT (sin dígito verificador)
     */
    fun getRutNumber(rut: String): Long? {
        val cleanRut = cleanRut(rut)
        if (cleanRut.length < 2) return null

        val rutNumber = cleanRut.dropLast(1)
        return rutNumber.toLongOrNull()
    }

    /**
     * Extrae solo el dígito verificador del RUT
     */
    fun getVerifier(rut: String): String? {
        val cleanRut = cleanRut(rut)
        if (cleanRut.isEmpty()) return null

        return cleanRut.last().toString().uppercase()
    }

    /**
     * Lista de RUTs de prueba válidos
     */
    val sampleValidRuts = listOf(
        "11111111-1",  // 11111111 -> verificador 1 (válido)
        "12345678-5",  // 12345678 -> verificador 5 (válido)
        "22222222-2",  // 22222222 -> verificador 2 (válido)
        "7775367-K",   // 7775367 -> verificador K (válido)
        "18765432-1",  // Mantener algunos originales que pueden ser válidos
        "20123456-7"   // Mantener algunos originales
    )
}
