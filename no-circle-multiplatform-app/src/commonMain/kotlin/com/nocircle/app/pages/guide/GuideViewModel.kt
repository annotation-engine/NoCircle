package com.nocircle.app.pages.guide

import com.nocircle.app.api.impls.authApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.compose.viewmodel.NoViewModel

class GuideViewModel : NoViewModel() {
	
	suspend fun verifyToken(): Boolean {
		val result = ktorfitx.authApi.verifyToken() ?: return networkError()
		return result.success
	}
}