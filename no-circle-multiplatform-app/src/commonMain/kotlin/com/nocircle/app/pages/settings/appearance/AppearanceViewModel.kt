package com.nocircle.app.pages.settings.appearance

import androidx.compose.animation.core.Animatable
import androidx.compose.ui.unit.Dp
import com.nocircle.app.theme.colors.*
import com.nocircle.common.config.ConfigKey
import com.nocircle.common.config.get
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking

class AppearanceViewModel : NoViewModel() {
	
	val colorSchemeAttribute by lazy { MutableStateFlow(getColorSchemeAttribute()) }
	
	val colorSchemeCardWidth = MutableStateFlow(Dp.Unspecified)
	
	val themeModeSystemPercent = Animatable(0f)
	
	val themeModeSystemFlag = MutableStateFlow(false)
	
	private fun getColorSchemeAttribute() = runBlocking(Dispatchers.IO) {
		val group = ColorSchemeGroupConfigKey.get()?.let { name -> ColorSchemeGroup.All.find { it.toString() == name } } ?: BlueColorSchemeGroup
		val contrast = ColorSchemeContrastConfigKey.get() ?: ColorSchemeContrast.Standard
		val themeMode = ColorSchemeThemeModeConfigKey.get() ?: ThemeMode.System
		ColorSchemeAttribute(group, contrast, themeMode)
	}
}

object ColorSchemeContrastConfigKey : ConfigKey<ColorSchemeContrast>

object ColorSchemeGroupConfigKey : ConfigKey<String>

object ColorSchemeThemeModeConfigKey : ConfigKey<ThemeMode>