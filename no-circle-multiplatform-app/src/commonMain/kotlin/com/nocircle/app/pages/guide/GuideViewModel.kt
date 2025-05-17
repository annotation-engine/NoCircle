package com.nocircle.app.pages.guide

import com.nocircle.app.api.impl.authApi
import com.nocircle.app.generated.resources.Res
import com.nocircle.app.generated.resources.global_network_connect_error
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.compose.viewmodel.NoViewModel

class GuideViewModel : NoViewModel() {
	
	suspend fun verifyToken(): Boolean {
		val result = ktorfitx.authApi.verifyToken() ?: let {
			showNoErrorSnackbar(Res.string.global_network_connect_error)
			return false
		}
		return result.success
	}
}