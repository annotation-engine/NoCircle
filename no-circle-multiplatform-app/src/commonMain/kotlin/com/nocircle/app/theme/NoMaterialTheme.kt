package com.nocircle.app.theme

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import com.nocircle.app.theme.colors.ThemeMode
import com.nocircle.app.theme.colors.getColorScheme

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
		colorScheme = animateColorScheme(colorScheme),
		content = content,
	)
}

expect fun onDarkModeChanged(isDarkTheme: Boolean)

@Composable
fun animateColorScheme(target: ColorScheme): ColorScheme {
	val transition = updateTransition(target, label = "ColorSchemeTransition")
	return ColorScheme(
		primary = transition.animateColor(label = "primary") { it.primary }.value,
		onPrimary = transition.animateColor(label = "onPrimary") { it.onPrimary }.value,
		primaryContainer = transition.animateColor(label = "primaryContainer") { it.primaryContainer }.value,
		onPrimaryContainer = transition.animateColor(label = "onPrimaryContainer") { it.onPrimaryContainer }.value,
		inversePrimary = transition.animateColor(label = "inversePrimary") { it.inversePrimary }.value,
		secondary = transition.animateColor(label = "secondary") { it.secondary }.value,
		onSecondary = transition.animateColor(label = "onSecondary") { it.onSecondary }.value,
		secondaryContainer = transition.animateColor(label = "secondaryContainer") { it.secondaryContainer }.value,
		onSecondaryContainer = transition.animateColor(label = "onSecondaryContainer") { it.onSecondaryContainer }.value,
		tertiary = transition.animateColor(label = "tertiary") { it.tertiary }.value,
		onTertiary = transition.animateColor(label = "onTertiary") { it.onTertiary }.value,
		tertiaryContainer = transition.animateColor(label = "tertiaryContainer") { it.tertiaryContainer }.value,
		onTertiaryContainer = transition.animateColor(label = "onTertiaryContainer") { it.onTertiaryContainer }.value,
		background = transition.animateColor(label = "background") { it.background }.value,
		onBackground = transition.animateColor(label = "onBackground") { it.onBackground }.value,
		surface = transition.animateColor(label = "surface") { it.surface }.value,
		onSurface = transition.animateColor(label = "onSurface") { it.onSurface }.value,
		surfaceVariant = transition.animateColor(label = "surfaceVariant") { it.surfaceVariant }.value,
		onSurfaceVariant = transition.animateColor(label = "onSurfaceVariant") { it.onSurfaceVariant }.value,
		surfaceTint = transition.animateColor(label = "surfaceTint") { it.surfaceTint }.value,
		inverseSurface = transition.animateColor(label = "inverseSurface") { it.inverseSurface }.value,
		inverseOnSurface = transition.animateColor(label = "inverseOnSurface") { it.inverseOnSurface }.value,
		error = transition.animateColor(label = "error") { it.error }.value,
		onError = transition.animateColor(label = "onError") { it.onError }.value,
		errorContainer = transition.animateColor(label = "errorContainer") { it.errorContainer }.value,
		onErrorContainer = transition.animateColor(label = "onErrorContainer") { it.onErrorContainer }.value,
		outline = transition.animateColor(label = "outline") { it.outline }.value,
		outlineVariant = transition.animateColor(label = "outlineVariant") { it.outlineVariant }.value,
		scrim = transition.animateColor(label = "scrim") { it.scrim }.value,
		surfaceBright = transition.animateColor(label = "surfaceBright") { it.surfaceBright }.value,
		surfaceDim = transition.animateColor(label = "surfaceDim") { it.surfaceDim }.value,
		surfaceContainer = transition.animateColor(label = "surfaceContainer") { it.surfaceContainer }.value,
		surfaceContainerHigh = transition.animateColor(label = "surfaceContainerHigh") { it.surfaceContainerHigh }.value,
		surfaceContainerHighest = transition.animateColor(label = "surfaceContainerHighest") { it.surfaceContainerHighest }.value,
		surfaceContainerLow = transition.animateColor(label = "surfaceContainerLow") { it.surfaceContainerLow }.value,
		surfaceContainerLowest = transition.animateColor(label = "surfaceContainerLowest") { it.surfaceContainerLowest }.value,
	)
}

@Composable
private fun animateColor(target: Color): Color {
	return animateColorAsState(target).value
}