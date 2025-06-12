package com.nocircle.common.resources

import com.nocircle.common.config.ConfigKey
import com.nocircle.common.config.get
import com.nocircle.common.config.set
import com.nocircle.common.flow.StatusFlowConfig

enum class SupportedLanguage(
	val language: String,
	val displayName: String
) {
	CHINESE(
		language = "zh",
		displayName = "简体中文",
	),
	ENGLISH(
		language = "en",
		displayName = "English",
	),
	JAPANESE(
		language = "ja",
		displayName = "日本語",
	),
	FRENCH(
		language = "fr",
		displayName = "Français"
	),
	GERMAN(
		language = "de",
		displayName = "Deutsch"
	),
	RUSSIAN(
		language = "ru",
		displayName = "Русский"
	);
	
	companion object : StatusFlowConfig<SupportedLanguage>() {
		
		override suspend fun getConfigFromStorage(): SupportedLanguage {
			return SupportedLanguageConfigKey.get() ?: CHINESE
		}
		
		override suspend fun setConfigToStorage(oldConfig: SupportedLanguage, newConfig: SupportedLanguage) {
			SupportedLanguageConfigKey.set(newConfig)
			clearSupportedLanguageCache(oldConfig)
		}
	}
}

private object SupportedLanguageConfigKey : ConfigKey<SupportedLanguage>("supportedLanguage")