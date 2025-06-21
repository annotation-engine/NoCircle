package com.nocircle.compose.resources

import com.nocircle.common.config.ConfigKey
import com.nocircle.common.config.get
import com.nocircle.common.config.set
import com.nocircle.compose.coroutines.StatusFlowConfig
import kotlinx.serialization.Serializable

@Serializable
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

private object SupportedLanguageConfigKey : ConfigKey<SupportedLanguage>("supportedLanguage", isOwn = true)