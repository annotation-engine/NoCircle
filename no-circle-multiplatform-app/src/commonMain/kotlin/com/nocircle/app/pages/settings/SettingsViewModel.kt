package com.nocircle.app.pages.settings

import androidx.lifecycle.viewModelScope
import com.nocircle.app.api.impl.userApi
import com.nocircle.app.config.LanguageConfigKey
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.resources.loadAppStringJson
import com.nocircle.common.config.TokenConfigKey
import com.nocircle.common.config.clear
import com.nocircle.common.config.get
import com.nocircle.common.config.set
import com.nocircle.common.resources.SupportLanguage
import com.nocircle.common.resources.getSupportLanguage
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : NoViewModel() {

    private val _language = MutableStateFlow(getSupportLanguage())
    val language: StateFlow<SupportLanguage> = _language

    init {
        viewModelScope.launch {
            initLanguage()
        }
    }

    private suspend fun initLanguage() {
        LanguageConfigKey.get()?.let(::getSupportLanguage)?.let {
            _language.value = it
            loadAppStringJson(it)
        }
    }

    suspend fun setLanguage(language: SupportLanguage) {
        _language.value = language
        LanguageConfigKey.set(language.language)
    }

    suspend fun logout() {
        ktorfitx.userApi.logout()
        TokenConfigKey.clear()
    }
}