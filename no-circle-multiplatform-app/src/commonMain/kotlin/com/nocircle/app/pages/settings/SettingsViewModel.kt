package com.nocircle.app.pages.settings

import androidx.lifecycle.viewModelScope
import com.nocircle.app.api.impl.userApi
import com.nocircle.app.config.FontWeightLevelConfigKey
import com.nocircle.app.config.IconTypeConfigKey
import com.nocircle.app.config.LanguageConfigKey
import com.nocircle.app.generated.resources.MiSans_VF
import com.nocircle.app.generated.resources.Res
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.common.config.TokenConfigKey
import com.nocircle.common.config.clear
import com.nocircle.common.config.get
import com.nocircle.common.config.set
import com.nocircle.common.resources.IconType
import com.nocircle.common.resources.SupportLanguage
import com.nocircle.common.resources.clearLanguageCache
import com.nocircle.common.resources.getSupportLanguage
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : NoViewModel() {
	
	private val _fontResource = MutableStateFlow(Res.font.MiSans_VF)
	val fontResource = _fontResource.asStateFlow()
	
	private val _fontWeightLevel = MutableStateFlow(FontWeightLevel.Medium)
	val fontWeightLevel = _fontWeightLevel.asStateFlow()
	
	init {
		viewModelScope.launch(Dispatchers.IO) {
			initLanguage()
			initFontWeightLevel()
			initIconType()
		}
	}
	
	private suspend fun initLanguage() {
		LanguageConfigKey.get()?.let(::getSupportLanguage)?.let {
			SupportLanguage.current.value = it
		}
	}
	
	private suspend fun initFontWeightLevel() {
		FontWeightLevelConfigKey.get()?.let {
			setFontWeightLevel(it)
		}
	}
	
	private suspend fun initIconType() {
		IconTypeConfigKey.get()?.let { name ->
			setIconType(IconType.entries.first { it.name == name })
		}
	}
	
	suspend fun setSupportLanguage(language: SupportLanguage) {
		val current = SupportLanguage.current.value
		if (current != language) {
			clearLanguageCache(current)
			SupportLanguage.current.value = language
			LanguageConfigKey.set(language.language)
		}
	}
	
	suspend fun setFontWeightLevel(ordinal: Int) {
		if (_fontWeightLevel.value.ordinal != ordinal) {
			_fontWeightLevel.value = FontWeightLevel.entries.find { it.ordinal == ordinal } ?: FontWeightLevel.Medium
			FontWeightLevelConfigKey.set(ordinal)
		}
	}
	
	suspend fun setIconType(iconType: IconType) {
		val current = IconType.current.value
		if (current != iconType) {
			IconType.current.value = iconType
			IconTypeConfigKey.set(iconType.name)
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