package com.nocircle.app.pages.settings

import com.nocircle.app.api.impls.userApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.common.config.TokenConfigKey
import com.nocircle.common.config.clear
import com.nocircle.common.expends.tryWithLock
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.sync.Mutex

class SettingsViewModel : NoViewModel() {
	
	private val logoutMutex = Mutex()
	
	suspend fun logout() {
		logoutMutex.tryWithLock {
			ktorfitx.userApi.logout()
			TokenConfigKey.clear()
		}
	}
}