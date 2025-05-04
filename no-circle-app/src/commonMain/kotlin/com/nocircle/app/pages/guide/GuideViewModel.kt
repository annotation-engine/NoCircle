package com.nocircle.app.pages.guide

import com.nocircle.app.http.ktorClient
import com.nocircle.app.utils.ConfigUtils
import com.nocircle.common.expends.safePost
import com.nocircle.compose.viewmodel.NoViewModel
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

class GuideViewModel : NoViewModel() {
	
	suspend fun verifyToken(): Boolean {
		val token = ConfigUtils.getValue<String>("token") ?: return false
		val result = ktorClient.safePost<String>("auth/verifyToken") {
			contentType(ContentType.MultiPart.FormData)
			val parts = parameters {
				append("token", token)
			}
			setBody(FormDataContent(parts))
		} ?: return false
		return result.success
	}
}