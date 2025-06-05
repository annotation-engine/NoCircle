package com.nocircle.app.pages.settings

import com.nocircle.app.api.impl.userApi
import com.nocircle.app.generated.resources.MiSans_VF
import com.nocircle.app.generated.resources.Res
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.common.config.*
import com.nocircle.common.resources.*
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking

class SettingsViewModel : NoViewModel() {
	
	private val _fontResource = MutableStateFlow(Res.font.MiSans_VF)
	val fontResource = _fontResource.asStateFlow()
	
	private val _fontWeightLevel by lazy { MutableStateFlow(getFontWeightLevel()) }
	val fontWeightLevel by lazy { _fontWeightLevel.asStateFlow() }
	
	
	private fun getFontWeightLevel() = runBlocking(Dispatchers.IO) {
		FontWeightLevelConfigKey.get() ?: FontWeightLevel.Medium
	}
	
	suspend fun setSupportLanguage(language: SupportedLanguage) {
		val current = SupportedLanguage.current.value
		if (current != language) {
			clearLanguageCache(current)
			SupportedLanguage.current.value = language
			SupportedLanguageConfigKey.set(language)
		}
	}
	
	suspend fun setFontWeightLevel(ordinal: Int) {
		if (_fontWeightLevel.value.ordinal != ordinal) {
			val level = FontWeightLevel.entries.find { it.ordinal == ordinal } ?: FontWeightLevel.Medium
			_fontWeightLevel.value = level
			FontWeightLevelConfigKey.set(level)
		}
	}
	
	suspend fun setIconType(iconType: IconType) {
		val current = IconType.current.value
		if (current != iconType) {
			IconType.current.value = iconType
			IconTypeConfigKey.set(iconType)
		}
	}
	
	suspend fun logout() {
		ktorfitx.userApi.logout()
		TokenConfigKey.clear()
	}
}

enum class FontWeightLevel(
	val progression: IntProgression
) {
	UltraThin(100..300 step 25),
	ExtraThin(100..500 step 50),
	Thin(100..700 step 75),
	Medium(100..900 step 100),
	Bold(300..900 step 75),
	ExtraBold(500..900 step 50),
	UltraBold(700..900 step 25)
}

object FontWeightLevelConfigKey : ConfigKey<FontWeightLevel>