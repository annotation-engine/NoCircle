package com.nocircle.app.theme

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.*
import androidx.compose.ui.unit.Dp
import com.nocircle.app.theme.scheme.ThemeMode
import com.nocircle.app.theme.scheme.getColorScheme
import com.nocircle.app.theme.shape.RoundedCornerType

@Composable
fun NoMaterialTheme(
	content: @Composable () -> Unit,
) {
	val isDark = ThemeMode.current.isDark
	LaunchedEffect(isDark) {
		onDarkModeChanged(isDark)
	}
	MaterialTheme(
		colorScheme = animateColorScheme(),
		shapes = animateShapes(),
		content = content,
	)
}

expect fun onDarkModeChanged(isDarkTheme: Boolean)

@Composable
private fun animateColorScheme(): ColorScheme {
	val target = getColorScheme()
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
private fun animateShapes(): Shapes {
	val target = RoundedCornerType.current
	val updateTransition = updateTransition(target, label = "CornersTransition")
	return Shapes(
		extraSmall = updateTransition.animateRoundedCornerShape { it.extraSmall }.value,
		small = updateTransition.animateRoundedCornerShape { it.small }.value,
		medium = updateTransition.animateRoundedCornerShape { it.medium }.value,
		large = updateTransition.animateRoundedCornerShape { it.large }.value,
		extraLarge = updateTransition.animateRoundedCornerShape { it.extraLarge }.value,
	)
}

@Composable
private inline fun <S> Transition<S>.animateRoundedCornerShape(
	noinline transitionSpec: @Composable Transition.Segment<S>.() -> FiniteAnimationSpec<Dp> = {
		spring(visibilityThreshold = Dp.VisibilityThreshold)
	},
	label: String = "RoundedCornerShapeAnimation",
	targetValueByState: @Composable() (state: S) -> Dp
): State<RoundedCornerShape> {
	val value by this.animateDp(transitionSpec, label, targetValueByState)
	return remember(value) {
		derivedStateOf { RoundedCornerShape(value) }
	}
}