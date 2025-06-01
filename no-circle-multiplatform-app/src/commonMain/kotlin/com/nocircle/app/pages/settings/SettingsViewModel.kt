package com.nocircle.app.pages.settings

import androidx.lifecycle.viewModelScope
import com.nocircle.app.api.impl.userApi
import com.nocircle.app.config.FontWeightLevelConfigKey
import com.nocircle.app.config.LanguageConfigKey
import com.nocircle.app.generated.resources.MiSans_VF
import com.nocircle.app.generated.resources.Res
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : NoViewModel() {

    private val _language = MutableStateFlow(getSupportLanguage())
    val language = _language.asStateFlow()

    private val _fontResource = MutableStateFlow(Res.font.MiSans_VF)
    val fontResource = _fontResource.asStateFlow()

    private val _fontWeightLevel = MutableStateFlow(FontWeightLevel.Medium)
    val fontWeightLevel = _fontWeightLevel.asStateFlow()

    init {
        viewModelScope.launch {
            initLanguage()
            initFontWeightLevel()
        }
    }

    private suspend fun initLanguage() {
        LanguageConfigKey.get()?.let(::getSupportLanguage)?.let {
            _language.value = it
            loadAppStringJson(it)
        }
    }

    private suspend fun initFontWeightLevel() {
        FontWeightLevelConfigKey.get()?.let {
            _fontWeightLevel.value = FontWeightLevel.entries[it]
        }
    }

    suspend fun setLanguage(language: SupportLanguage) {
        _language.value = language
        LanguageConfigKey.set(language.language)
    }

    suspend fun setFontWeightLevel(value: FontWeightLevel) {
        _fontWeightLevel.value = value
        FontWeightLevelConfigKey.set(value.ordinal)
    }

    suspend fun logout() {
        ktorfitx.userApi.logout()
        TokenConfigKey.clear()
    }
}