package com.nocircle.app.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.nocircle.app.generated.resources.MiSans_VF
import com.nocircle.app.generated.resources.Res
import com.nocircle.app.theme.colors.ColorSchemeContrast
import com.nocircle.app.theme.colors.ColorSchemeGroup
import com.nocircle.app.theme.colors.DefaultColorSchemeGroup
import com.nocircle.app.theme.colors.getColorScheme
import com.nocircle.app.theme.typographies.getNoTypography

@Composable
fun NoMaterialTheme(
	content: @Composable () -> Unit,
) {
	val darkTheme = isSystemInDarkTheme()
	LaunchedEffect(Unit) {
		NoThemes.darkTheme = darkTheme
	}
	
	val colorScheme = NoThemes.colorSchemeGroup.getColorScheme(
		contrast = NoThemes.colorSchemeContrast,
		isDark = darkTheme
	)
	val typography = getNoTypography(fontResource = NoThemes.fontResource)
	MaterialTheme(
		colorScheme = colorScheme,
		typography = typography,
		content = content
	)
}

object NoThemes {
	
	val colorSchemeGroup by mutableStateOf<ColorSchemeGroup>(DefaultColorSchemeGroup)
	
	val colorSchemeContrast by mutableStateOf(ColorSchemeContrast.Standard)
	
	var darkTheme by mutableStateOf(false)
	
	var fontResource by mutableStateOf(Res.font.MiSans_VF)
}