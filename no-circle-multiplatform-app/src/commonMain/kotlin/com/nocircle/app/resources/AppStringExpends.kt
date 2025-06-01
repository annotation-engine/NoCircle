package com.nocircle.app.resources

import com.nocircle.common.resources.SupportLanguage
import com.nocircle.common.resources.loadStringJson

suspend fun loadAppStringJson(language: SupportLanguage) {
    loadStringJson(
        language = language,
        packageName = "com.nocircle.app"
    )
}