package com.nocircle.app.theme.colors

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.*
import com.nocircle.app.resources.AppString
import com.nocircle.app.theme.colors.ColorSchemeContrast.*
import com.nocircle.common.config.ConfigKey
import com.nocircle.common.config.get
import com.nocircle.common.config.set
import com.nocircle.common.flow.StatusFlowConfig

@Immutable
sealed interface ColorSchemeGroup {
	
	val name: AppString
	
	val lightStandardContrast: ColorScheme
	
	val darkStandardContrast: ColorScheme
	
	val lightMediumContrast: ColorScheme
	
	val darkMediumContrast: ColorScheme
	
	val lightHighContrast: ColorScheme
	
	val darkHighContrast: ColorScheme
	
	companion object : StatusFlowConfig<ColorSchemeGroup>() {
		
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
		
		override suspend fun getConfigFromStorage(): ColorSchemeGroup {
			val name = ColorSchemeGroupConfigKey.get<AppString>() ?: RedColorSchemeGroup.name
			return allColorSchemeGroups.first { it.name == name }
		}
		
		override suspend fun setConfigToStorage(oldConfig: ColorSchemeGroup, newConfig: ColorSchemeGroup) {
			ColorSchemeGroupConfigKey.set(newConfig.name)
		}
	}
}

enum class ColorSchemeContrast(
	val title: AppString,
) {
	Standard(AppString.AppearanceContrastStandard),
	Medium(AppString.AppearanceContrastMedium),
	High(AppString.AppearanceContrastHigh);
	
	companion object : StatusFlowConfig<ColorSchemeContrast>() {
		
		override suspend fun getConfigFromStorage(): ColorSchemeContrast {
			return ColorSchemeContrastConfigKey.get() ?: Standard
		}
		
		override suspend fun setConfigToStorage(oldConfig: ColorSchemeContrast, newConfig: ColorSchemeContrast) {
			ColorSchemeContrastConfigKey.set(newConfig)
		}
	}
}

enum class ThemeMode(
	val title: AppString
) {
	Light(AppString.AppearanceThemeModeLight),
	Dark(AppString.AppearanceThemeModeDark),
	System(AppString.AppearanceThemeModeSystem);
	
	companion object : StatusFlowConfig<ThemeMode>() {
		
		override suspend fun getConfigFromStorage(): ThemeMode {
			return ThemeModeConfigKey.get() ?: System
		}
		
		override suspend fun setConfigToStorage(oldConfig: ThemeMode, newConfig: ThemeMode) {
			ThemeModeConfigKey.set(newConfig)
		}
	}
	
	val isDark: Boolean
		@Composable
		@ReadOnlyComposable
		get() = when (this) {
			Light -> false
			Dark -> true
			System -> isSystemInDarkTheme()
		}
}

private object ColorSchemeContrastConfigKey : ConfigKey<ColorSchemeContrast>("colorSchemeContrast")

private object ColorSchemeGroupConfigKey : ConfigKey<AppString>("colorSchemeGroup")

private object ThemeModeConfigKey : ConfigKey<ThemeMode>("themeMode")

@Stable
@Composable
fun getColorScheme(
	group: ColorSchemeGroup = ColorSchemeGroup.current,
	contrast: ColorSchemeContrast = ColorSchemeContrast.current,
	themeMode: ThemeMode = ThemeMode.current
): ColorScheme {
	val isDark = themeMode.isDark
	return remember(group, contrast, isDark) {
		when (contrast) {
			Standard -> if (isDark) group.darkStandardContrast else group.lightStandardContrast
			Medium -> if (isDark) group.darkMediumContrast else group.lightMediumContrast
			High -> if (isDark) group.darkHighContrast else group.lightHighContrast
		}
	}
}