package com.nocircle.app.pages.settings

import com.nocircle.app.generated.resources.MiSans_VF
import com.nocircle.app.generated.resources.Res
import com.nocircle.app.theme.colors.ColorSchemeContrast
import com.nocircle.app.theme.colors.ColorSchemeGroup
import com.nocircle.app.theme.colors.ColorSchemeGroup1
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsViewModel : NoViewModel() {
	
	val colorSchemeContrast = MutableStateFlow(ColorSchemeContrast.Standard)
	
	val colorSchemeGroup = MutableStateFlow<ColorSchemeGroup>(ColorSchemeGroup1)
	
	val darkTheme = MutableStateFlow(false)
	
	val fontResource = MutableStateFlow(Res.font.MiSans_VF)
}