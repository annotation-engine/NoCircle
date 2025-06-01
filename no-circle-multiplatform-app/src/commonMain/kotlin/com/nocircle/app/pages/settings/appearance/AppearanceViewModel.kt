package com.nocircle.app.pages.settings.appearance

import androidx.compose.animation.core.Animatable
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewModelScope
import com.nocircle.app.config.ColorSchemeContrastConfigKey
import com.nocircle.app.config.ColorSchemeGroupConfigKey
import com.nocircle.app.config.ColorSchemeThemeModeConfigKey
import com.nocircle.app.theme.colors.*
import com.nocircle.common.config.get
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AppearanceViewModel : NoViewModel() {

    val colorSchemeAttribute =
        MutableStateFlow(ColorSchemeAttribute(BlueColorSchemeGroup, ColorSchemeContrast.Standard, ThemeMode.System))

    val colorSchemeCardWidth = MutableStateFlow(Dp.Unspecified)

    val themeModeSystemPercent = Animatable(0f)

    val themeModeSystemFlag = MutableStateFlow(false)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            initValue()
        }
    }

    private suspend fun initValue() {
        val contrast = ColorSchemeContrastConfigKey.get().let { name ->
            ColorSchemeContrast.entries.find { it.name == name }
        } ?: ColorSchemeContrast.Standard
        val group = ColorSchemeGroupConfigKey.get()?.let { name ->
            ColorSchemeGroup.All.find { it.toString() == name }
        } ?: BlueColorSchemeGroup
        val themeMode = ColorSchemeThemeModeConfigKey.get()?.let { name ->
            ThemeMode.entries.find { it.name == name }
        } ?: ThemeMode.System
        colorSchemeAttribute.value = colorSchemeAttribute.value.copy(group, contrast, themeMode)
    }
}