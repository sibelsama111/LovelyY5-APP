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
        // Cargar desde assets al iniciar el repositorio
        try {
            val jsonText = context.assets.open("ProductosDisponibles.json").bufferedReader().use { it.readText() }
            val dtos = Json.decodeFromString<List<ProductDto>>(jsonText)
            val mapped = dtos.mapIndexed { index, dto ->
                Product(
                    id = index + 1,
                    name = dto.nombre ?: dto.codigo ?: "Producto $index",
                    description = dto.descripcion ?: "",
                    price = dto.precio ?: 0.0,
                    category = dto.categoria ?: "",
                    images = dto.imagenes ?: emptyList(),
                    stock = dto.stock ?: 0,
                    specs = dto.specs ?: ""
                )
            }
            state.value = mapped
        } catch (e: Exception) {
            // Si ocurre cualquier error, dejamos la lista vacía
            e.printStackTrace()
            state.value = emptyList()
        }
    }

    fun getProducts() = state

    fun getProductById(id: Int): Product? = state.value.find { it.id == id }
}
