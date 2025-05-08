package com.nocircle.app.theme.colors

import androidx.compose.material3.ColorScheme

interface ColorSchemeGroup {
	
	val lightStandardContrast: ColorScheme
	
	val darkStandardContrast: ColorScheme
	
	val lightMediumContrast: ColorScheme
	
	val darkMediumContrast: ColorScheme
	
	val lightHighContrast: ColorScheme
	
	val darkHighContrast: ColorScheme
}

fun ColorSchemeGroup.getColorScheme(
	contrast: ColorSchemeContrast,
	isDark: Boolean
): ColorScheme = when (contrast) {
	ColorSchemeContrast.Standard -> if (isDark) darkStandardContrast else lightStandardContrast
	ColorSchemeContrast.Medium -> if (isDark) darkMediumContrast else lightMediumContrast
	ColorSchemeContrast.High -> if (isDark) darkHighContrast else lightHighContrast
}

enum class ColorSchemeContrast {
	
	Standard,
	
	Medium,
	
	High
}