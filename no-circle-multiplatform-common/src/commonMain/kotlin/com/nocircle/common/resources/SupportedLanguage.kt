package com.nocircle.common.resources

import com.nocircle.common.config.ConfigKey
import com.nocircle.common.config.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking

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
	
	companion object Companion {
		
		val current by lazy { MutableStateFlow(getSupportLanguage()) }
		
		private fun getSupportLanguage(): SupportedLanguage = runBlocking(Dispatchers.IO) {
			SupportedLanguageConfigKey.get() ?: Chinese
		}
	}
}

object SupportedLanguageConfigKey : ConfigKey<SupportedLanguage>