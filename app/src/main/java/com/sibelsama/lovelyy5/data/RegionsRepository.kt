package com.sibelsama.lovelyy5.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class RegionEntry(val name: String, val comunas: List<String>)

class RegionsRepository(private val context: Context) {
    suspend fun loadRegions(): List<RegionEntry> = withContext(Dispatchers.IO) {
        try {
            val input = context.assets.open("regions.json").bufferedReader().use { it.readText() }
            Json.decodeFromString<List<RegionEntry>>(input)
        } catch (e: Exception) {
            android.util.Log.e("RegionsRepository", "Error loading regions.json", e)
            emptyList()
        }
    }
}
