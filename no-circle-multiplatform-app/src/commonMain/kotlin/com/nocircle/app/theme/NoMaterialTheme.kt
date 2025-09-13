package com.nocircle.app.theme

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.nocircle.app.theme.scheme.ColorSchemeConfig
import com.nocircle.app.theme.scheme.animateColorScheme
import com.nocircle.app.theme.type.animateShapes
import com.nocircle.app.theme.type.getTypography

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NoMaterialTheme(
	content: @Composable () -> Unit,
) {
	val isDark = ColorSchemeConfig.current.themeMode.isDark
	LaunchedEffect(isDark) {
		onDarkModeChanged(isDark)
	}
	MaterialExpressiveTheme(
		colorScheme = animateColorScheme(),
		motionScheme = MotionScheme.standard(),
		shapes = animateShapes(),
		typography = getTypography(),
		content = content,
	)
}

expect fun onDarkModeChanged(isDarkTheme: Boolean)