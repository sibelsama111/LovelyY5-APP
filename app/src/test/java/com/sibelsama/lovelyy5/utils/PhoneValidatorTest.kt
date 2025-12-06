package com.sibelsama.lovelyy5.utils

import org.junit.Test
import org.junit.Assert.*

class PhoneValidatorTest {

    @Test
    fun `isValidChileanPhone should return true for valid 9-digit phones`() {
        val validPhones = listOf(
            "912345678", // Celular
            "987654321", // Celular
            "956788901", // Celular
            "923456789", // Celular
            "234567890", // Fijo Santiago
            "322334455", // Fijo Valparaíso
            "412233445", // Fijo Concepción
            "552334567"  // Fijo Norte
        )

        validPhones.forEach { phone ->
            assertTrue("Phone $phone should be valid", PhoneValidator.isValidChileanPhone(phone))
        }
    }

    @Test
    fun `isValidChileanPhone should return false for invalid phones`() {
        val invalidPhones = listOf(
            "12345678",   // 8 digits
            "1234567890", // 10 digits
            "02345678",   // Starts with 0
            "123456789",  // Starts with 1
            "abc123456",  // Contains letters
            "",           // Empty
            "9123-4567",  // 8 digits with dash
            "+56912345678" // With country code (11 digits total)
        )

        invalidPhones.forEach { phone ->
            assertFalse("Phone $phone should be invalid", PhoneValidator.isValidChileanPhone(phone))
        }
    }

    @Test
    fun `formatChileanPhone should add country code correctly`() {
        assertEquals("+56912345678", PhoneValidator.formatChileanPhone("912345678"))
        assertEquals("+56234567890", PhoneValidator.formatChileanPhone("234567890"))
        assertEquals("+56987654321", PhoneValidator.formatChileanPhone("987654321"))
    }

    @Test
    fun `formatForDisplay should format with spaces correctly`() {
        assertEquals("+56 9 1234 5678", PhoneValidator.formatForDisplay("912345678"))
        assertEquals("+56 2 3456 7890", PhoneValidator.formatForDisplay("234567890"))
        assertEquals("+56 9 8765 4321", PhoneValidator.formatForDisplay("987654321"))
    }

    @Test
    fun `isCellPhone should identify cell phones correctly`() {
        assertTrue(PhoneValidator.isCellPhone("912345678"))
        assertTrue(PhoneValidator.isCellPhone("987654321"))
        assertFalse(PhoneValidator.isCellPhone("234567890"))
        assertFalse(PhoneValidator.isCellPhone("322334455"))
    }

    @Test
    fun `isLandline should identify landlines correctly`() {
        assertFalse(PhoneValidator.isLandline("912345678"))
        assertFalse(PhoneValidator.isLandline("987654321"))
        assertTrue(PhoneValidator.isLandline("234567890"))
        assertTrue(PhoneValidator.isLandline("322334455"))
    }

    @Test
    fun `generateSamplePhone should generate valid phone`() {
        val samplePhone = PhoneValidator.generateSamplePhone()
        assertEquals(9, samplePhone.length)
        assertTrue("Generated phone should be valid", PhoneValidator.isValidChileanPhone(samplePhone))
    }

    @Test
    fun `sampleValidPhones should all be valid`() {
        PhoneValidator.sampleValidPhones.forEach { phone ->
            assertTrue("Sample phone $phone should be valid", PhoneValidator.isValidChileanPhone(phone))
            assertEquals("Sample phone should have 9 digits", 9, phone.length)
        }
    }

    @Test
    fun `formatChileanPhone should handle invalid input gracefully`() {
        assertEquals("12345", PhoneValidator.formatChileanPhone("12345")) // Too short
        assertEquals("1234567890", PhoneValidator.formatChileanPhone("1234567890")) // Too long
        assertEquals("", PhoneValidator.formatChileanPhone("")) // Empty
    }

    @Test
    fun `formatForDisplay should handle invalid input gracefully`() {
        assertEquals("12345", PhoneValidator.formatForDisplay("12345")) // Too short
        assertEquals("1234567890", PhoneValidator.formatForDisplay("1234567890")) // Too long
        assertEquals("", PhoneValidator.formatForDisplay("")) // Empty
    }

    @Test
    fun `phone cleaning should remove non-digits`() {
        assertTrue(PhoneValidator.isValidChileanPhone("9-1234-5678"))
        assertTrue(PhoneValidator.isValidChileanPhone("9 1234 5678"))
        assertTrue(PhoneValidator.isValidChileanPhone("(9) 1234-5678"))
        assertTrue(PhoneValidator.isValidChileanPhone("912.345.678"))
    }
}
