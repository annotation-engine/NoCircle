package com.nocircle.compose.resources

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.intl.Locale

enum class SupportLanguage(
    val language: String,
    val displayName: String,
) {
    Chinese(
        language = "zh",
        displayName = "简体中文",
    ),
    English(
        language = "en",
        displayName = "English",
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