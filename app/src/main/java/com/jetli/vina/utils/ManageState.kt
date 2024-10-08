package com.jetli.vina.utils

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Defining possible tabs
enum class Tab {
    HOME, CARD, SAVING, SALARY, MAIN
}

class GloballyViewModel : ViewModel() {
    // States
    private val _mainModalActive = MutableStateFlow(false)
    val mainModalActive: StateFlow<Boolean> = _mainModalActive

    private val _activeTab = MutableStateFlow(Tab.HOME)
    val activeTab: StateFlow<Tab> = _activeTab

    private val _imageUrl = MutableStateFlow<String?>(null)
    val imageUrl: StateFlow<String?> = _imageUrl

    // Actions (equivalent to reducers)
    fun setMainModalActive(isActive: Boolean) {
        _mainModalActive.value = isActive
    }

    fun setActiveTab(tab: Tab) {
        _activeTab.value = tab
    }

    fun saveImageCaptured(imageUrl: String) {
        _imageUrl.value = imageUrl
    }
}
