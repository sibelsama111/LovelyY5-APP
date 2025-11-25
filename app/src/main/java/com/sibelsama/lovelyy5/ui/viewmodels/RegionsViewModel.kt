package com.sibelsama.lovelyy5.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sibelsama.lovelyy5.data.RegionEntry
import com.sibelsama.lovelyy5.data.RegionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegionsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RegionsRepository(application.applicationContext)

    private val _regions = MutableStateFlow<List<RegionEntry>>(emptyList())
    val regions: StateFlow<List<RegionEntry>> = _regions.asStateFlow()

    init {
        viewModelScope.launch {
            val loaded = repository.loadRegions()
            _regions.value = loaded
            android.util.Log.d("RegionsViewModel", "Loaded ${loaded.size} regions from assets")
        }
    }

    fun getComunasFor(regionName: String): List<String> = _regions.value.find { it.name == regionName }?.comunas ?: emptyList()
}

