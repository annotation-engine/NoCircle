package com.nocircle.app.pages.settings

import com.nocircle.app.api.impl.userApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.common.config.TokenConfigKey
import com.nocircle.common.config.clear
import com.nocircle.compose.viewmodel.NoViewModel

class SettingsViewModel : NoViewModel() {
	
	suspend fun logout() {
		ktorfitx.userApi.logout()
		TokenConfigKey.clear()
	}
}