package com.nocircle.app.theme.groups

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import com.nocircle.app.resources.AppString
import com.nocircle.app.theme.groups.ColorSchemeContrast.*
import com.nocircle.common.config.ConfigKey
import com.nocircle.common.config.get
import com.nocircle.common.config.set
import com.nocircle.common.flow.StatusFlowConfig
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

@Serializable
enum class ColorSchemeContrast(
	val title: AppString,
) {
	STANDARD(AppString.APPEARANCE_CONTRAST_STANDARD),
	MEDIUM(AppString.APPEARANCE_CONTRAST_MEDIUM),
	HIGH(AppString.APPEARANCE_CONTRAST_HIGH);
	
	companion object : StatusFlowConfig<ColorSchemeContrast>() {
		
		override suspend fun getConfigFromStorage(): ColorSchemeContrast {
			return ColorSchemeContrastConfigKey.get() ?: STANDARD
		}
		
		override suspend fun setConfigToStorage(oldConfig: ColorSchemeContrast, newConfig: ColorSchemeContrast) {
			ColorSchemeContrastConfigKey.set(newConfig)
		}
	}
}

@Serializable
enum class ThemeMode(
	val title: AppString
) {
	LIGHT(AppString.APPEARANCE_THEME_MODE_LIGHT),
	DARK(AppString.APPEARANCE_THEME_MODE_DARK),
	SYSTEM(AppString.APPEARANCE_THEME_MODE_SYSTEM);
	
	companion object : StatusFlowConfig<ThemeMode>() {
		
		override suspend fun getConfigFromStorage(): ThemeMode {
			return ThemeModeConfigKey.get() ?: SYSTEM
		}
		
		override suspend fun setConfigToStorage(oldConfig: ThemeMode, newConfig: ThemeMode) {
			ThemeModeConfigKey.set(newConfig)
		}
	}
	
	val isDark: Boolean
		@Composable
		@ReadOnlyComposable
		get() = when (this) {
			LIGHT -> false
			DARK -> true
			SYSTEM -> isSystemInDarkTheme()
		}
}

private object ColorSchemeContrastConfigKey : ConfigKey<ColorSchemeContrast>("colorSchemeContrast")

private object ColorSchemeGroupConfigKey : ConfigKey<AppString>("colorSchemeGroup")

private object ThemeModeConfigKey : ConfigKey<ThemeMode>("themeMode")

@Composable
fun getColorScheme(
	group: ColorSchemeGroup = ColorSchemeGroup.current,
	contrast: ColorSchemeContrast = ColorSchemeContrast.current,
	themeMode: ThemeMode = ThemeMode.current
): ColorScheme {
	val isDark = themeMode.isDark
	return remember(group, contrast, isDark) {
		when (contrast) {
			STANDARD -> if (isDark) group.darkStandardContrast else group.lightStandardContrast
			MEDIUM -> if (isDark) group.darkMediumContrast else group.lightMediumContrast
			HIGH -> if (isDark) group.darkHighContrast else group.lightHighContrast
		}
	}
}