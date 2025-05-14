package com.nocircle.app.theme.colors

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import com.nocircle.app.generated.resources.*
import org.jetbrains.compose.resources.StringResource

sealed interface ColorSchemeGroup {
	
	val name: StringResource
	
	val lightStandardContrast: ColorScheme
	
	val darkStandardContrast: ColorScheme
	
	val lightMediumContrast: ColorScheme
	
	val darkMediumContrast: ColorScheme
	
	val lightHighContrast: ColorScheme
	
	val darkHighContrast: ColorScheme
	
	companion object {
		
		val All by lazy {
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

fun ColorSchemeGroup.getColorScheme(
	contrast: ColorSchemeContrast,
	isDark: Boolean
): ColorScheme = when (contrast) {
	ColorSchemeContrast.Standard -> if (isDark) darkStandardContrast else lightStandardContrast
	ColorSchemeContrast.Medium -> if (isDark) darkMediumContrast else lightMediumContrast
	ColorSchemeContrast.High -> if (isDark) darkHighContrast else lightHighContrast
}

enum class ColorSchemeContrast(
	val title: StringResource,
) {
	Standard(Res.string.settings_contrast_standard),
	Medium(Res.string.settings_contrast_medium),
	High(Res.string.settings_contrast_high),
}

enum class ThemeMode(
	val title: StringResource
) {
	Light(Res.string.settings_theme_mode_light),
	Dark(Res.string.settings_theme_mode_dark),
	System(Res.string.settings_theme_mode_system);
	
	val isDark: Boolean
		@Composable
		get() = when (this) {
			Light -> false
			Dark -> true
			System -> isSystemInDarkTheme()
		}
}