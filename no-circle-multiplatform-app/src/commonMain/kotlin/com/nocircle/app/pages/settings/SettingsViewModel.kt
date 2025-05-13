package com.nocircle.app.pages.settings

import androidx.lifecycle.viewModelScope
import com.nocircle.app.constants.ColorSchemeContrastConfigKey
import com.nocircle.app.constants.ColorSchemeDarkModeConfigKey
import com.nocircle.app.constants.ColorSchemeGroupConfigKey
import com.nocircle.app.generated.resources.MiSans_VF
import com.nocircle.app.generated.resources.Res
import com.nocircle.app.theme.colors.BlueColorSchemeGroup
import com.nocircle.app.theme.colors.ColorSchemeContrast
import com.nocircle.app.theme.colors.ColorSchemeGroup
import com.nocircle.app.theme.colors.ThemeMode
import com.nocircle.common.config.get
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : NoViewModel() {
	
	val colorSchemeContrast = MutableStateFlow(ColorSchemeContrast.Standard)
	
	val colorSchemeGroup = MutableStateFlow<ColorSchemeGroup>(BlueColorSchemeGroup)
	
	val themeMode = MutableStateFlow(ThemeMode.System)
	
	val fontResource = MutableStateFlow(Res.font.MiSans_VF)
	
	init {
		viewModelScope.launch(Dispatchers.IO) {
			initContrast()
			initGroup()
			initDarkMode()
		}
	}
	
	private suspend fun initContrast() {
		ColorSchemeContrastConfigKey.get().let { name ->
			ColorSchemeContrast.entries.find { it.name == name }
		}?.let {
			colorSchemeContrast.value = it
		}
	}
	
	private suspend fun initGroup() {
		ColorSchemeGroupConfigKey.get()?.let { name ->
			ColorSchemeGroup.All.find { it.toString() == name }
		}?.let {
			colorSchemeGroup.value = it
		}
	}
	
	private suspend fun initDarkMode() {
		ColorSchemeDarkModeConfigKey.get()?.let { name ->
			ThemeMode.entries.find { it.name == name }
		}?.let {
			themeMode.value = it
		}
	}
}