package com.sibelsama.lovelyy5.utils

import org.junit.Test
import org.junit.Assert.*

class RutValidatorTest {

    @Test
    fun `isValidRut should return true for valid RUTs`() {
        val validRuts = listOf(
            "11111111-1",
            "12345678-5",
            "22222222-2",
            "7775367-K"
        )

        validRuts.forEach { rut ->
            assertTrue("RUT $rut should be valid", RutValidator.isValidRut(rut))
        }
    }

    @Test
    fun `isValidRut should return false for invalid RUTs`() {
        val invalidRuts = listOf(
            "12345678-9", // Wrong verifier
            "11111111-2", // Wrong verifier
            "123456789",  // No dash
            "1234567-8",  // Too short
            "abc12345-6", // Contains letters
            "",           // Empty
            "12345678-",  // Missing verifier
            "-5"          // Missing number
        )

        invalidRuts.forEach { rut ->
            assertFalse("RUT $rut should be invalid", RutValidator.isValidRut(rut))
        }
    }

    @Test
    fun `formatRut should format RUT correctly`() {
        assertEquals("12345678-5", RutValidator.formatRut("123456785"))
        assertEquals("12345678-5", RutValidator.formatRut("12345678-5"))
        assertEquals("12345678-K", RutValidator.formatRut("12345678k"))
        assertEquals("12345678-K", RutValidator.formatRut("12.345.678-k"))
    }

    @Test
    fun `getRutNumber should extract number correctly`() {
        assertEquals(12345678L, RutValidator.getRutNumber("12345678-5"))
        assertEquals(11111111L, RutValidator.getRutNumber("11111111-1"))
        assertNull(RutValidator.getRutNumber(""))
        assertNull(RutValidator.getRutNumber("a"))
    }

    @Test
    fun `getVerifier should extract verifier correctly`() {
        assertEquals("5", RutValidator.getVerifier("12345678-5"))
        assertEquals("K", RutValidator.getVerifier("12345678-k"))
        assertEquals("K", RutValidator.getVerifier("12345678K"))
        assertNull(RutValidator.getVerifier(""))
    }

    @Test
    fun `generateSampleRut should generate valid RUT`() {
        val sampleRut = RutValidator.generateSampleRut()
        assertTrue("Generated RUT should be valid", RutValidator.isValidRut(sampleRut))
        assertTrue("Generated RUT should have correct format", sampleRut.matches(Regex("\\d{8}-[0-9K]")))
    }

    @Test
    fun `sampleValidRuts should all be valid`() {
        RutValidator.sampleValidRuts.forEach { rut ->
            assertTrue("Sample RUT $rut should be valid", RutValidator.isValidRut(rut))
        }
    }

    @Test
    fun `calculateVerifier should work for known cases`() {
        // Test internal logic through public methods
        assertTrue("Known valid RUT", RutValidator.isValidRut("12345678-5"))
        assertTrue("Known valid RUT", RutValidator.isValidRut("11111111-1"))
        assertTrue("Known valid RUT with K", RutValidator.isValidRut("7775367-K"))
    }
}
