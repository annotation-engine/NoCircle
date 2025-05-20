package com.nocircle.app.pages.settings

import com.nocircle.app.api.impl.userApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.compose.viewmodel.NoViewModel

class SettingsViewModel : NoViewModel() {
	
	suspend fun logout(): Boolean {
		val result = ktorfitx.userApi.logout() ?: return false
		return result.success
	}
}