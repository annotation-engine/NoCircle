package com.nocircle.common.resources

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.intl.Locale

enum class SupportLanguage(
    val language: String,
    val displayName: String,
    internal val stringCacheMap: MutableMap<String, Map<String, String>> = mutableMapOf()
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
    );

    companion object {
        lateinit var current: SupportLanguage
    }
}

fun getSupportLanguage(
    language: String = Locale.current.language
): SupportLanguage {
    return SupportLanguage.entries.find { it.language == language } ?: SupportLanguage.Chinese
}

val LocalSupportLanguage = compositionLocalOf { getSupportLanguage() }