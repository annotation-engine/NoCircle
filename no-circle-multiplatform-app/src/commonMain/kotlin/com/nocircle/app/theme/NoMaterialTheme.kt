package com.nocircle.app.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.nocircle.app.pages.settings.SettingsViewModel
import com.nocircle.app.theme.colors.getColorScheme
import com.nocircle.app.theme.typographies.getNoTypography
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NoMaterialTheme(
	content: @Composable () -> Unit,
) {
	val settingsViewModel = koinViewModel<SettingsViewModel>()
	val darkTheme = isSystemInDarkTheme()
	LaunchedEffect(Unit) {
		settingsViewModel.darkTheme.value = darkTheme
	}
	val colorSchemeContrast by settingsViewModel.colorSchemeContrast.collectAsState()
	val colorSchemeGroup by settingsViewModel.colorSchemeGroup.collectAsState()
	val colorScheme = colorSchemeGroup.getColorScheme(
		contrast = colorSchemeContrast,
		isDark = darkTheme
	)
	val fontResource by settingsViewModel.fontResource.collectAsState()
	val typography = getNoTypography(fontResource = fontResource)
	MaterialTheme(
		colorScheme = colorScheme,
		typography = typography,
		content = content
	)
}