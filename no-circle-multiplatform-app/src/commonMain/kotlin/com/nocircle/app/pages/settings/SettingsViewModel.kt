package com.nocircle.app.pages.settings

import com.nocircle.app.api.impls.userApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.app.pages.account.login.LoginRoute
import com.nocircle.common.config.TokenConfigKey
import com.nocircle.common.config.UserIdConfigKey
import com.nocircle.common.config.clear
import com.nocircle.common.coroutines.FunctionLocker
import com.nocircle.compose.navigation.getNavController
import com.nocircle.compose.viewmodel.NoViewModel

class SettingsViewModel : NoViewModel() {
	
	suspend fun logout() {
		FunctionLocker.tryWithLock(::logout) {
			val result = ktorfitx.userApi.logout() ?: networkError()
			if (result.success) {
				TokenConfigKey.clear()
				UserIdConfigKey.clear()
				getNavController().navigate(LoginRoute) {
					launchSingleTop = true
					popUpTo(0) {
						inclusive = true
					}
				}
			}
		}
	}
}