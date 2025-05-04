package com.nocircle.app.pages.guide

import com.nocircle.app.http.ktorClient
import com.nocircle.app.utils.ConfigUtils
import com.nocircle.common.expends.safePost
import com.nocircle.common.viewmodel.NoViewModel

class GuideViewModel : NoViewModel() {
	
	suspend fun verifyToken(): Boolean {
		val token = ConfigUtils.getValue<String>("token")
		ktorClient.safePost<String>("/auth/verifyToken")
		return false
	}
}