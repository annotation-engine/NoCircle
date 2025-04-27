package com.nocircle.app.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.nocircle.app.theme.colors.ColorSchemeContrast
import com.nocircle.app.theme.colors.ColorSchemeGroup
import com.nocircle.app.theme.colors.DefaultColorSchemeGroup
import com.nocircle.app.theme.colors.getColorScheme

@Composable
fun NoMaterialTheme(
	content: @Composable () -> Unit
) {
	val darkTheme = isSystemInDarkTheme()
	LaunchedEffect(Unit) {
		NoThemes.darkTheme = darkTheme
	}
	
	val colorScheme = NoThemes.colorSchemeGroup.getColorScheme(
		contrast = NoThemes.colorSchemeContrast,
		isDark = darkTheme
	)
	MaterialTheme(
		colorScheme = colorScheme,
		content = content
	)
}

object NoThemes {
	
	val colorSchemeGroup by mutableStateOf<ColorSchemeGroup>(DefaultColorSchemeGroup)
	
	val colorSchemeContrast by mutableStateOf(ColorSchemeContrast.Standard)
	
	var darkTheme by mutableStateOf(false)
}