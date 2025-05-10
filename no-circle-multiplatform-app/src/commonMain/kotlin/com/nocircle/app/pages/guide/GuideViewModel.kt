package com.nocircle.app.pages.guide

import com.nocircle.app.http.ktorClient
import com.nocircle.common.ktor.safePost
import com.nocircle.compose.viewmodel.NoViewModel

class GuideViewModel : NoViewModel() {
	
	suspend fun verifyToken(): Boolean {
		val result = ktorClient.safePost<String>("auth/verifyToken")
		return result?.success ?: false
	}
}