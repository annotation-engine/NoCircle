package com.nocircle.common.resources

import com.nocircle.common.config.ConfigKey
import com.nocircle.common.config.get
import com.nocircle.common.config.set
import com.nocircle.common.flow.StatusFlowConfig

enum class SupportedLanguage(
	val language: String,
	val displayName: String
) {
	Chinese(
		language = "zh",
		displayName = "简体中文",
	),
	English(
		language = "en",
		displayName = "English",
	),
	Japanese(
		language = "ja",
		displayName = "日本語",
	),
	French(
		language = "fr",
		displayName = "Français"
	),
	German(
		language = "de",
		displayName = "Deutsch"
	),
	Russian(
		language = "ru",
		displayName = "Русский"
	);
	
	companion object : StatusFlowConfig<SupportedLanguage>() {
		
		override suspend fun getConfigFromStorage(): SupportedLanguage {
			return SupportedLanguageConfigKey.get() ?: Chinese
		}
		
		override suspend fun setConfigToStorage(oldConfig: SupportedLanguage, newConfig: SupportedLanguage) {
			SupportedLanguageConfigKey.set(newConfig)
			clearSupportedLanguageCache(oldConfig)
		}
	}
}

private object SupportedLanguageConfigKey : ConfigKey<SupportedLanguage>("supportedLanguage")