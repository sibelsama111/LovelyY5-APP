package com.sibelsama.lovelyy5.utils

import org.junit.Test
import org.junit.Assert.*

class PriceFormatterTest {

    @Test
    fun `formatAsNumber should format price with thousands separator`() {
        assertEquals("899.990", PriceFormatter.formatAsNumber(899990.0))
        assertEquals("45.990", PriceFormatter.formatAsNumber(45990.0))
        assertEquals("1.500.000", PriceFormatter.formatAsNumber(1500000.0))
    }

    @Test
    fun `formatWithSymbol should add peso symbol`() {
        assertEquals("$899.990", PriceFormatter.formatWithSymbol(899990.0))
        assertEquals("$45.990", PriceFormatter.formatWithSymbol(45990.0))
        assertEquals("$1.500.000", PriceFormatter.formatWithSymbol(1500000.0))
    }

    @Test
    fun `calculateDiscountPercentage should return correct percentage`() {
        assertEquals(20, PriceFormatter.calculateDiscountPercentage(1000000.0, 800000.0))
        assertEquals(10, PriceFormatter.calculateDiscountPercentage(100000.0, 90000.0))
        assertEquals(0, PriceFormatter.calculateDiscountPercentage(100000.0, 100000.0))
        assertEquals(0, PriceFormatter.calculateDiscountPercentage(100000.0, 120000.0))
    }

    @Test
    fun `calculateSavings should return correct savings amount`() {
        assertEquals("$200.000", PriceFormatter.calculateSavings(1000000.0, 800000.0))
        assertEquals("$10.000", PriceFormatter.calculateSavings(100000.0, 90000.0))
        assertEquals("$0", PriceFormatter.calculateSavings(100000.0, 100000.0))
        assertEquals("$0", PriceFormatter.calculateSavings(100000.0, 120000.0))
    }

    @Test
    fun `convertFromUSD should convert correctly`() {
        assertEquals(900000.0, PriceFormatter.convertFromUSD(1000.0), 0.01)
        assertEquals(450000.0, PriceFormatter.convertFromUSD(500.0), 0.01)
    }

    @Test
    fun `convertToUSD should convert correctly`() {
        assertEquals(1000.0, PriceFormatter.convertToUSD(900000.0), 0.01)
        assertEquals(500.0, PriceFormatter.convertToUSD(450000.0), 0.01)
    }

    @Test
    fun `isValidCLPPrice should validate price ranges`() {
        assertTrue(PriceFormatter.isValidCLPPrice(50000.0))
        assertTrue(PriceFormatter.isValidCLPPrice(1000000.0))
        assertFalse(PriceFormatter.isValidCLPPrice(50.0)) // Too low
        assertFalse(PriceFormatter.isValidCLPPrice(100_000_000.0)) // Too high
    }

    @Test
    fun `formatWithDiscount should format discount info correctly`() {
        val result = PriceFormatter.formatWithDiscount(1000000.0, 800000.0)
        assertTrue(result.contains("Antes: $1.000.000"))
        assertTrue(result.contains("Ahora: $800.000"))
        assertTrue(result.contains("20% OFF"))
    }

    @Test
    fun `formatWithDiscount should handle no discount`() {
        val result = PriceFormatter.formatWithDiscount(100000.0, 100000.0)
        assertEquals("$100.000", result)
    }
}
