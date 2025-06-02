package com.nocircle.common.resources

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.intl.Locale

enum class SupportLanguage(
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

    companion object {
        var current = Chinese
    }
}

fun getSupportLanguage(
    language: String = Locale.current.language
): SupportLanguage {
    return SupportLanguage.entries.find { it.language == language } ?: SupportLanguage.Chinese
}

val LocalSupportLanguage = compositionLocalOf { getSupportLanguage() }