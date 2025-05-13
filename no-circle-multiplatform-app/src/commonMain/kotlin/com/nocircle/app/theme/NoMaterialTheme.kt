package com.nocircle.app.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
	MaterialTheme(
		colorScheme = getColorScheme(),
		typography = getNoTypography(),
		content = content
	)
}

@Composable
fun getColorScheme(): ColorScheme {
	val viewModel = koinViewModel<SettingsViewModel>()
	val colorSchemeContrast by viewModel.colorSchemeContrast.collectAsState()
	val themeMode by viewModel.themeMode.collectAsState()
	val colorSchemeGroup by viewModel.colorSchemeGroup.collectAsState()
	val isDark = themeMode.isDark
	return colorSchemeGroup.getColorScheme(
		contrast = colorSchemeContrast,
		isDark = isDark
	)
}