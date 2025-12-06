package com.sibelsama.lovelyy5.utils

import java.text.NumberFormat
import java.util.Locale

/**
 * Utilidades para formatear precios en pesos chilenos (CLP)
 */
object PriceFormatter {

    private val chileanLocale = Locale.Builder().setLanguage("es").setRegion("CL").build()
    private val currencyFormatter = NumberFormat.getCurrencyInstance(chileanLocale)
    private val numberFormatter = NumberFormat.getNumberInstance(chileanLocale)

    /**
     * Formatea un precio como moneda chilena
     * Ejemplo: 899990 -> $899.990
     */
    fun formatAsCurrency(price: Double): String {
        return currencyFormatter.format(price).replace("CLP", "").trim()
    }

    /**
     * Formatea un precio como número con separadores de miles
     * Ejemplo: 899990 -> 899.990
     */
    fun formatAsNumber(price: Double): String {
        return numberFormatter.format(price)
    }

    /**
     * Formatea un precio con símbolo de peso chileno
     * Ejemplo: 899990 -> $899.990
     */
    fun formatWithSymbol(price: Double): String {
        return "$${formatAsNumber(price)}"
    }

    /**
     * Calcula descuento porcentual
     * Ejemplo: (precio original: 999990, precio actual: 799990) -> 20%
     */
    fun calculateDiscountPercentage(originalPrice: Double, currentPrice: Double): Int {
        if (originalPrice <= 0 || currentPrice >= originalPrice) return 0
        return ((originalPrice - currentPrice) / originalPrice * 100).toInt()
    }

    /**
     * Calcula el ahorro en CLP
     * Ejemplo: (precio original: 999990, precio actual: 799990) -> $200.000
     */
    fun calculateSavings(originalPrice: Double, currentPrice: Double): String {
        val savings = originalPrice - currentPrice
        return if (savings > 0) formatWithSymbol(savings) else "$0"
    }

    /**
     * Convierte de USD a CLP (tasa aproximada)
     * Nota: Para producción usar una API de cambio real
     */
    fun convertFromUSD(usdPrice: Double, exchangeRate: Double = 900.0): Double {
        return usdPrice * exchangeRate
    }

    /**
     * Convierte de CLP a USD (tasa aproximada)
     */
    fun convertToUSD(clpPrice: Double, exchangeRate: Double = 900.0): Double {
        return clpPrice / exchangeRate
    }

    /**
     * Verifica si un precio está en rango válido para CLP
     */
    fun isValidCLPPrice(price: Double): Boolean {
        return price >= 100 && price <= 50_000_000 // Entre $100 y $50M
    }

    /**
     * Formatea precio con descuento destacado
     * Ejemplo: "Antes: $999.990 | Ahora: $799.990 (20% OFF)"
     */
    fun formatWithDiscount(originalPrice: Double, currentPrice: Double): String {
        val discount = calculateDiscountPercentage(originalPrice, currentPrice)
        return if (discount > 0) {
            "Antes: ${formatWithSymbol(originalPrice)} | Ahora: ${formatWithSymbol(currentPrice)} (${discount}% OFF)"
        } else {
            formatWithSymbol(currentPrice)
        }
    }
}
