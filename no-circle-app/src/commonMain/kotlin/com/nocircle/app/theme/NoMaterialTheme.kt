package com.nocircle.app.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Composable
fun NoMaterialTheme(
	content: @Composable () -> Unit
) {
	val darkTheme = isSystemInDarkTheme()
	LaunchedEffect(Unit) {
		NoThemes.darkTheme = darkTheme
	}
	
	val colorScheme = NoThemes.colorSchemes.let {
		if (darkTheme) it.darkColorScheme else it.lightColorScheme
	}
	MaterialTheme(
		colorScheme = colorScheme,
		content = content
	)
}

object NoThemes {
	
	var colorSchemes by mutableStateOf<ColorSchemes>(DefaultColorSchemes)
	
	var darkTheme by mutableStateOf(false)
}