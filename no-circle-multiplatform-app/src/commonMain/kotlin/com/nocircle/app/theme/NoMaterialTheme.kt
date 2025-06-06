package com.nocircle.app.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.nocircle.app.pages.settings.appearance.AppearanceViewModel
import com.nocircle.app.theme.colors.ThemeMode
import com.nocircle.app.theme.colors.getColorScheme
import com.nocircle.app.theme.typographies.getNoTypography
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NoMaterialTheme(
	content: @Composable () -> Unit,
) {
	val colorScheme = getColorScheme()
	val isDark = ThemeMode.current.isDark
	LaunchedEffect(isDark) {
		onDarkModeChanged(isDark)
	}
	MaterialTheme(
		colorScheme = animateColorSchemeTransition(colorScheme),
		typography = getNoTypography(),
		content = content,
	)
}

expect fun onDarkModeChanged(isDarkTheme: Boolean)

@Composable
fun animateColorSchemeTransition(target: ColorScheme): ColorScheme {
	return ColorScheme(
		primary = animateColor(target.primary),
		onPrimary = animateColor(target.onPrimary),
		primaryContainer = animateColor(target.primaryContainer),
		onPrimaryContainer = animateColor(target.onPrimaryContainer),
		inversePrimary = animateColor(target.inversePrimary),
		secondary = animateColor(target.secondary),
		onSecondary = animateColor(target.onSecondary),
		secondaryContainer = animateColor(target.secondaryContainer),
		onSecondaryContainer = animateColor(target.onSecondaryContainer),
		tertiary = animateColor(target.tertiary),
		onTertiary = animateColor(target.onTertiary),
		tertiaryContainer = animateColor(target.tertiaryContainer),
		onTertiaryContainer = animateColor(target.onTertiaryContainer),
		background = animateColor(target.background),
		onBackground = animateColor(target.onBackground),
		surface = animateColor(target.surface),
		onSurface = animateColor(target.onSurface),
		surfaceVariant = animateColor(target.surfaceVariant),
		onSurfaceVariant = animateColor(target.onSurfaceVariant),
		surfaceTint = animateColor(target.surfaceTint),
		inverseSurface = animateColor(target.inverseSurface),
		inverseOnSurface = animateColor(target.inverseOnSurface),
		error = animateColor(target.error),
		onError = animateColor(target.onError),
		errorContainer = animateColor(target.errorContainer),
		onErrorContainer = animateColor(target.onErrorContainer),
		outline = animateColor(target.outline),
		outlineVariant = animateColor(target.outlineVariant),
		scrim = animateColor(target.scrim),
		surfaceBright = animateColor(target.surfaceBright),
		surfaceDim = animateColor(target.surfaceDim),
		surfaceContainer = animateColor(target.surfaceContainer),
		surfaceContainerHigh = animateColor(target.surfaceContainerHigh),
		surfaceContainerHighest = animateColor(target.surfaceContainerHighest),
		surfaceContainerLow = animateColor(target.surfaceContainerLow),
		surfaceContainerLowest = animateColor(target.surfaceContainerLowest),
	)
}

@Composable
private fun animateColor(target: Color): Color {
	val animated by animateColorAsState(target)
	return animated
}