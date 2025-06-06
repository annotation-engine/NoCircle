package com.nocircle.app.theme.typography

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.util.fastMapIndexed
import com.nocircle.app.generated.resources.MiSans_VF
import com.nocircle.app.generated.resources.Res
import com.nocircle.app.pages.settings.FontWeightLevel
import org.jetbrains.compose.resources.Font

@Composable
fun getTypography(
	typography: Typography = MaterialTheme.typography,
	fontFamily: FontFamily = getFontFamily()
): Typography = with(typography) {
	Typography(
		displayLarge = displayLarge.copy(fontFamily = fontFamily),
		displayMedium = displayMedium.copy(fontFamily = fontFamily),
		displaySmall = displaySmall.copy(fontFamily = fontFamily),
		headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
		headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
		headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
		titleLarge = titleLarge.copy(fontFamily = fontFamily),
		titleMedium = titleMedium.copy(fontFamily = fontFamily),
		titleSmall = titleSmall.copy(fontFamily = fontFamily),
		bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
		bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
		bodySmall = bodySmall.copy(fontFamily = fontFamily),
		labelLarge = labelLarge.copy(fontFamily = fontFamily),
		labelMedium = labelMedium.copy(fontFamily = fontFamily),
		labelSmall = labelSmall.copy(fontFamily = fontFamily)
	)
}

private val FontWeightList = (100..900 step 100).map { FontWeight(it) }

@Composable
private fun getFontFamily(): FontFamily {
	val progression = FontWeightLevel.current.progression
	val settings = remember(progression) {
		progression.map { FontVariation.Settings(FontVariation.weight(it)) }
	}
	return FontFamily(
		fonts = settings.fastMapIndexed { index, settings ->
			Font(
				resource = MiSansVF,
				weight = FontWeightList[index],
				variationSettings = settings
			)
		}
	)
}

private val MiSansVF = Res.font.MiSans_VF