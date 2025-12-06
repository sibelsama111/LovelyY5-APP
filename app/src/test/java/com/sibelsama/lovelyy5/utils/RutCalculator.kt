package com.sibelsama.lovelyy5.utils

/**
 * Calculadora simple de RUT para generar RUTs válidos para testing
 */
object RutCalculator {

    fun calculateVerifier(rutNumber: String): String {
        val rut = rutNumber.toLong()
        var sum = 0L
        var multiplier = 2L
        var tempRut = rut

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

    fun generateValidRut(base: String): String {
        val verifier = calculateVerifier(base)
        return "$base-$verifier"
    }

    // Generar algunos RUTs válidos conocidos
    fun getKnownValidRuts(): List<String> {
        return listOf(
            generateValidRut("11111111"), // 11111111-1
            generateValidRut("12345678"), // 12345678-5
            generateValidRut("22222222"), // 22222222-2
            generateValidRut("7775367"),  // 7775367-K
            generateValidRut("18765432"), // Calculado
            generateValidRut("20123456")  // Calculado
        )
    }
}
