package com.nocircle.app.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.nocircle.app.theme.scheme.ColorSchemeConfig
import com.nocircle.app.theme.scheme.animateColorScheme
import com.nocircle.app.theme.type.animateShapes
import com.nocircle.app.theme.type.getTypography

@Composable
fun NoMaterialTheme(
	content: @Composable () -> Unit,
) {
	val isDark = ColorSchemeConfig.current.themeMode.isDark
	LaunchedEffect(isDark) {
		onDarkModeChanged(isDark)
	}
	MaterialTheme(
		colorScheme = animateColorScheme(),
		shapes = animateShapes(),
		typography = getTypography(),
		content = content,
	)
}

expect fun onDarkModeChanged(isDarkTheme: Boolean)