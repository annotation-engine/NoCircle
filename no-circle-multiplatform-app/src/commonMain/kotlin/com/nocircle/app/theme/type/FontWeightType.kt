package com.nocircle.app.theme.type

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.nocircle.app.generated.resources.MiSans_VF
import com.nocircle.app.generated.resources.Res
import com.nocircle.app.pages.settings.memory.freeMemory
import com.nocircle.common.config.ConfigKey
import com.nocircle.common.config.get
import com.nocircle.common.config.set
import com.nocircle.compose.coroutines.StatusFlowConfig
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.Font

@Serializable
enum class FontWeightType(
	val offsetWeight: Int
) {
	EXTRA_LIGHT(-300),
	LIGHT(-200),
	NORMAL(-100),
	BOLD(0),
	EXTRA_BOLD(100);
	
	companion object Companion : StatusFlowConfig<FontWeightType>() {
		override suspend fun getConfigFromStorage(): FontWeightType {
			return FontWeightTypeConfigKey.get() ?: NORMAL
		}
		
		override suspend fun setConfigToStorage(oldConfig: FontWeightType, newConfig: FontWeightType) {
			FontWeightTypeConfigKey.set(newConfig)
		}
	}
}

private object FontWeightTypeConfigKey : ConfigKey<FontWeightType>("fontWeightType", isOwn = true)

private var currentType: FontWeightType? = null
private var currentTypography: Typography? = null

@Composable
fun getTypography(): Typography = with(MaterialTheme.typography) {
	val type = FontWeightType.current
	if (currentType == type && currentTypography != null) {
		return currentTypography!!
	}
	this.copy(
		displayLarge = this.displayLarge.calcTextStyle(type),
		displayMedium = this.displayMedium.calcTextStyle(type),
		displaySmall = this.displaySmall.calcTextStyle(type),
		headlineLarge = this.headlineLarge.calcTextStyle(type),
		headlineMedium = this.headlineMedium.calcTextStyle(type),
		headlineSmall = this.headlineSmall.calcTextStyle(type),
		titleLarge = this.titleLarge.calcTextStyle(type),
		titleMedium = this.titleMedium.calcTextStyle(type),
		titleSmall = this.titleSmall.calcTextStyle(type),
		bodyLarge = this.bodyLarge.calcTextStyle(type),
		bodyMedium = this.bodyMedium.calcTextStyle(type),
		bodySmall = this.bodySmall.calcTextStyle(type),
		labelLarge = this.labelLarge.calcTextStyle(type),
		labelMedium = this.labelMedium.calcTextStyle(type),
		labelSmall = this.labelSmall.calcTextStyle(type),
	).also {
		currentType = type
		currentTypography = it
		val coroutineScope = rememberCoroutineScope()
		coroutineScope.launch {
			freeMemory()
		}
	}
}

@Composable
private fun TextStyle.calcTextStyle(type: FontWeightType): TextStyle {
	val fontWeight = FontWeight(((this.fontWeight ?: FontWeight.Normal).weight + type.offsetWeight).coerceIn(100..900))
	return this.copy(
		fontWeight = fontWeight,
		fontFamily = FontFamily(
			Font(
				resource = Res.font.MiSans_VF,
				weight = fontWeight
			)
		)
	)
}