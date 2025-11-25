package com.sibelsama.lovelyy5.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sibelsama.lovelyy5.model.ShippingDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "lovelyy5_preferences")

class DataManager(private val context: Context) {

    companion object {
        // Keys para las preferencias
        private val FIRST_TIME_KEY = booleanPreferencesKey("first_time")
        private val PERMISSIONS_GRANTED_KEY = booleanPreferencesKey("permissions_granted")
        private val SHIPPING_DETAILS_KEY = stringPreferencesKey("shipping_details")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val CART_ITEMS_KEY = stringPreferencesKey("cart_items")
        private val LAST_SEARCH_KEY = stringPreferencesKey("last_search")
        private val FAVORITE_PRODUCTS_KEY = stringPreferencesKey("favorite_products")
    }

    /**
     * Verifica si es la primera vez que se abre la app
     */
    val isFirstTime: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[FIRST_TIME_KEY] ?: true
    }

    /**
     * Marca que la app ya se ha abierto antes
     */
    suspend fun setNotFirstTime() {
        context.dataStore.edit { preferences ->
            preferences[FIRST_TIME_KEY] = false
        }
    }

    /**
     * Verifica si los permisos han sido concedidos
     */
    val arePermissionsGranted: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PERMISSIONS_GRANTED_KEY] ?: false
    }

    /**
     * Guarda el estado de los permisos
     */
    suspend fun setPermissionsGranted(granted: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PERMISSIONS_GRANTED_KEY] = granted
        }
    }

    /**
     * Guarda los datos de envío del usuario
     */
    suspend fun saveShippingDetails(shippingDetails: ShippingDetails) {
        try {
            val json = Json.encodeToString(shippingDetails)
            context.dataStore.edit { preferences ->
                preferences[SHIPPING_DETAILS_KEY] = json
            }
        } catch (e: Exception) {
            // Manejo de error silencioso para evitar crashes
        }
    }

    /**
     * Obtiene los datos de envío guardados
     */
    val shippingDetails: Flow<ShippingDetails?> = context.dataStore.data.map { preferences ->
        try {
            preferences[SHIPPING_DETAILS_KEY]?.let { json ->
                Json.decodeFromString<ShippingDetails>(json)
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Guarda el nombre del usuario para reviews
     */
    suspend fun saveUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }

    /**
     * Obtiene el nombre del usuario guardado
     */
    val userName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_NAME_KEY] ?: ""
    }

    /**
     * Guarda los items del carrito (como JSON)
     */
    suspend fun saveCartItems(cartItemsJson: String) {
        context.dataStore.edit { preferences ->
            preferences[CART_ITEMS_KEY] = cartItemsJson
        }
    }

    /**
     * Obtiene los items del carrito guardados
     */
    val cartItems: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[CART_ITEMS_KEY] ?: ""
    }

    /**
     * Guarda la última búsqueda del usuario
     */
    suspend fun saveLastSearch(query: String) {
        context.dataStore.edit { preferences ->
            preferences[LAST_SEARCH_KEY] = query
        }
    }

    /**
     * Obtiene la última búsqueda
     */
    val lastSearch: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LAST_SEARCH_KEY] ?: ""
    }

    /**
     * Guarda los productos favoritos (como lista de IDs separados por coma)
     */
    suspend fun saveFavoriteProducts(productIds: List<Int>) {
        val idsString = productIds.joinToString(",")
        context.dataStore.edit { preferences ->
            preferences[FAVORITE_PRODUCTS_KEY] = idsString
        }
    }

    /**
     * Obtiene la lista de productos favoritos
     */
    val favoriteProducts: Flow<List<Int>> = context.dataStore.data.map { preferences ->
        try {
            val idsString = preferences[FAVORITE_PRODUCTS_KEY] ?: ""
            if (idsString.isBlank()) {
                emptyList()
            } else {
                idsString.split(",").mapNotNull { it.toIntOrNull() }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Limpia todos los datos guardados (para reiniciar la app)
     */
    suspend fun clearAllData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
