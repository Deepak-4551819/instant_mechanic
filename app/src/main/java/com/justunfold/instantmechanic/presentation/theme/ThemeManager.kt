package com.justunfold.instantmechanic.presentation.theme

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ThemeMode { SYSTEM, LIGHT, DARK }

object ThemeManager {
    private val _themeState = MutableStateFlow(ThemeMode.SYSTEM)
    val themeState = _themeState.asStateFlow()

    fun setTheme(mode: ThemeMode) {
        _themeState.value = mode
    }
}
