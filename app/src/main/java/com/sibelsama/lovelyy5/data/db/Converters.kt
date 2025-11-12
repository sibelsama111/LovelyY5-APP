package com.sibelsama.lovelyy5.data.db

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object Converters {
    @TypeConverter
    @JvmStatic
    fun fromStringToMap(raw: String?): Map<Int, Int> = try {
        raw?.let { Json.decodeFromString(it) } ?: emptyMap()
    } catch (_: Exception) {
        emptyMap()
    }

    @TypeConverter
    @JvmStatic
    fun fromMapToString(map: Map<Int, Int>?): String = Json.encodeToString(map ?: emptyMap())

    @TypeConverter
    @JvmStatic
    fun fromStringList(raw: String?): List<String> = try {
        raw?.let { Json.decodeFromString(it) } ?: emptyList()
    } catch (_: Exception) {
        emptyList()
    }

    @TypeConverter
    @JvmStatic
    fun toStringList(list: List<String>?): String = Json.encodeToString(list ?: emptyList())
}
