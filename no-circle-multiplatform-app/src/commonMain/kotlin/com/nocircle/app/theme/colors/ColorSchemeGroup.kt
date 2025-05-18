package com.nocircle.app.theme.colors

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.nocircle.app.generated.resources.*
import com.nocircle.app.theme.colors.ColorSchemeContrast.*
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

enum class ColorSchemeContrast(
	val title: StringResource,
) {
	Standard(Res.string.appearance_contrast_standard),
	Medium(Res.string.appearance_contrast_medium),
	High(Res.string.appearance_contrast_high),
}

enum class ThemeMode(
	val title: StringResource
) {
	Light(Res.string.appearance_theme_mode_light),
	Dark(Res.string.appearance_theme_mode_dark),
	System(Res.string.appearance_theme_mode_system);
	
	companion object {
		
		fun getThemeMode(isDark: Boolean) = if (isDark) Dark else Light
	}
	
	val isDark: Boolean
		@Composable
		get() = when (this) {
			Light -> false
			Dark -> true
			System -> isSystemInDarkTheme()
		}
}

data class ColorSchemeAttribute(
	val group: ColorSchemeGroup,
	val contrast: ColorSchemeContrast,
	val themeMode: ThemeMode
) {
	
	@Composable
	fun getColorScheme(
		group: ColorSchemeGroup = this.group,
		contrast: ColorSchemeContrast = this.contrast,
		themeMode: ThemeMode = this.themeMode
	): State<ColorScheme> {
		val isDark = themeMode.isDark
		return remember(group, contrast, isDark) {
			derivedStateOf {
				if (isDark) {
					when (contrast) {
						Standard -> group.darkStandardContrast
						Medium -> group.darkMediumContrast
						High -> group.darkHighContrast
					}
				} else {
					when (contrast) {
						Standard -> group.lightStandardContrast
						Medium -> group.lightMediumContrast
						High -> group.lightHighContrast
					}
				}
			}
		}
	}
}