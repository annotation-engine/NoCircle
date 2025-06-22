package com.nocircle.app.theme.scheme

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.*
import com.nocircle.app.pages.settings.memory.freeMemory
import com.nocircle.app.resources.AppString
import com.nocircle.app.theme.scheme.ColorSchemeContrast.*
import com.nocircle.common.config.ConfigKey
import com.nocircle.common.config.getOrNull
import com.nocircle.common.config.set
import com.nocircle.compose.coroutines.StatusFlowConfig
import kotlinx.serialization.Serializable

@Immutable
sealed interface ColorSchemeGroup {
	
	val name: AppString
	
	val lightStandardContrast: ColorScheme
	
	val darkStandardContrast: ColorScheme
	
	val lightMediumContrast: ColorScheme
	
	val darkMediumContrast: ColorScheme
	
	val lightHighContrast: ColorScheme
	
	val darkHighContrast: ColorScheme
	
	companion object {
		
		val allColorSchemeGroups by lazy {
			listOf(
				RedColorSchemeGroup,
				PurpleColorSchemeGroup,
				ModenaColorSchemeGroup,
				BlueColorSchemeGroup,
				LightBlueColorSchemeGroup,
				CyanColorSchemeGroup,
				TurquoiseColorSchemeGroup,
				GreenColorSchemeGroup,
				LightGreenColorSchemeGroup,
				StoneGrayColorSchemeGroup,
				YellowColorSchemeGroup,
				AmberColorSchemeGroup,
			)
		}
	}
}

@Serializable
enum class ColorSchemeContrast(
	val title: AppString,
) {
	STANDARD(AppString.APPEARANCE_CONTRAST_STANDARD),
	MEDIUM(AppString.APPEARANCE_CONTRAST_MEDIUM),
	HIGH(AppString.APPEARANCE_CONTRAST_HIGH)
}

@Serializable
enum class ThemeMode(
	val title: AppString
) {
	LIGHT(AppString.APPEARANCE_THEME_MODE_LIGHT),
	DARK(AppString.APPEARANCE_THEME_MODE_DARK),
	SYSTEM(AppString.APPEARANCE_THEME_MODE_SYSTEM);
	
	val isDark: Boolean
		@Composable
		@ReadOnlyComposable
		get() = when (this) {
			LIGHT -> false
			DARK -> true
			SYSTEM -> isSystemInDarkTheme()
		}
}

object ColorSchemeConfigKey : ConfigKey<ColorSchemeConfig>("colorScheme")

@Immutable
@Serializable
data class ColorSchemeConfig(
	val contrast: ColorSchemeContrast,
	val group: AppString,
	val themeMode: ThemeMode,
) {
	
	companion object : StatusFlowConfig<ColorSchemeConfig>() {
		override suspend fun getConfigFromStorage(): ColorSchemeConfig {
			return ColorSchemeConfigKey.getOrNull() ?: ColorSchemeConfig(STANDARD, BlueColorSchemeGroup.name, ThemeMode.SYSTEM)
		}
		
		override suspend fun setConfigToStorage(oldConfig: ColorSchemeConfig, newConfig: ColorSchemeConfig) {
			ColorSchemeConfigKey.set(newConfig)
		}
	}
}

@Composable
fun getColorScheme(
	group: ColorSchemeGroup? = null,
	contrast: ColorSchemeContrast? = null,
	themeMode: ThemeMode? = null
): ColorScheme {
	val config = ColorSchemeConfig.current
	val group = group ?: ColorSchemeGroup.allColorSchemeGroups.first { it.name == config.group }
	val contrast = contrast ?: config.contrast
	val themeMode = themeMode ?: config.themeMode
	val isDark = themeMode.isDark
	return remember(group, contrast, isDark) {
		when (contrast) {
			STANDARD -> if (isDark) group.darkStandardContrast else group.lightStandardContrast
			MEDIUM -> if (isDark) group.darkMediumContrast else group.lightMediumContrast
			HIGH -> if (isDark) group.darkHighContrast else group.lightHighContrast
		}
	}
}

private var currentColorSchemeConfig: ColorSchemeConfig? = null
private var currentColorScheme: ColorScheme? = null

@Composable
fun animateColorScheme(): ColorScheme {
	val config = ColorSchemeConfig.current
	if (currentColorScheme != null && currentColorScheme == config) {
		return currentColorScheme!!
	}
	val target = getColorScheme()
	val transition = updateTransition(target, label = "ColorSchemeTransition")
	LaunchedEffect(transition.isRunning) {
		if (!transition.isRunning) {
			currentColorSchemeConfig = config
			currentColorScheme = transition.targetState
			freeMemory()
		}
	}
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