package com.sibelsama.lovelyy5.repository

import android.content.Context
import com.sibelsama.lovelyy5.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
private data class ProductDto(
    val codigo: String? = null,
    val nombre: String? = null,
    val descripcion: String? = null,
    val precio: Double? = null,
    val imagenes: List<String>? = null,
    val stock: Int? = null,
    val specs: String? = null,
    val categoria: String? = null
)

class ProductRepository(private val context: Context) {
    private val state = MutableStateFlow<List<Product>>(emptyList())

    init {
        loadProducts()
    }

    private fun loadProducts() {
        try {
            // Intentar cargar desde assets
            val jsonText = context.assets.open("ProductosDisponibles.json").bufferedReader().use { it.readText() }
            val dtos = Json.decodeFromString<List<ProductDto>>(jsonText)
            val mapped = dtos.mapIndexedNotNull { index, dto ->
                try {
                    Product(
                        id = index + 1,
                        name = dto.nombre?.takeIf { it.isNotBlank() } ?: dto.codigo?.takeIf { it.isNotBlank() } ?: "Producto ${index + 1}",
                        description = dto.descripcion?.takeIf { it.isNotBlank() } ?: "Descripción no disponible",
                        price = dto.precio?.takeIf { it >= 0 } ?: 0.0,
                        category = dto.categoria?.takeIf { it.isNotBlank() } ?: "Sin categoría",
                        images = dto.imagenes?.filter { it.isNotBlank() } ?: emptyList(),
                        stock = dto.stock?.takeIf { it >= 0 } ?: 0,
                        specs = dto.specs?.takeIf { it.isNotBlank() } ?: ""
                    )
                } catch (e: Exception) {
                    // Si un producto específico tiene error, lo omitimos
                    null
                }
            }

            if (mapped.isNotEmpty()) {
                state.value = mapped
            } else {
                // Si no hay productos válidos, cargar productos de ejemplo
                loadFallbackProducts()
            }
        } catch (e: Exception) {
            // Si hay error leyendo el archivo, cargar productos de ejemplo
            e.printStackTrace()
            loadFallbackProducts()
        }
    }

    private fun loadFallbackProducts() {
        // Productos de ejemplo para evitar que la app esté vacía
        state.value = listOf(
            Product(
                id = 1,
                name = "iPhone 14",
                description = "Smartphone Apple con chip A15 Bionic",
                price = 850000.0,
                category = "Celulares",
                images = emptyList(),
                stock = 10,
                specs = "128GB, 6.1 pulgadas, Cámara dual"
            ),
            Product(
                id = 2,
                name = "Samsung Galaxy S23",
                description = "Smartphone Samsung con pantalla Dynamic AMOLED",
                price = 750000.0,
                category = "Celulares",
                images = emptyList(),
                stock = 8,
                specs = "256GB, 6.1 pulgadas, Cámara triple"
            ),
            Product(
                id = 3,
                name = "iPad Air",
                description = "Tablet Apple con chip M1",
                price = 650000.0,
                category = "Tablets",
                images = emptyList(),
                stock = 5,
                specs = "64GB, 10.9 pulgadas, Wi-Fi"
            )
        )
    }

    fun getProducts() = state

    fun getProductById(id: Int): Product? {
        return try {
            state.value.find { it.id == id }
        } catch (e: Exception) {
            null
        }
    }

    fun getProductsByCategory(category: String): List<Product> {
        return try {
            if (category.isBlank()) {
                state.value
            } else {
                state.value.filter { it.category.equals(category, ignoreCase = true) }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun searchProducts(query: String): List<Product> {
        return try {
            if (query.isBlank()) {
                state.value
            } else {
                state.value.filter {
                    it.name.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true)
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
