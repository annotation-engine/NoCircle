package com.nocircle.app.http.api

import com.nocircle.app.http.ktorClient
import com.nocircle.common.expends.ApiResult
import com.nocircle.common.expends.safePost
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

object UserApi {
	
	suspend fun login(username: String, password: String): ApiResult<Login>? {
		return ktorClient.safePost("user/login") {
			contentType(ContentType.MultiPart.FormData)
			val parts = formData {
				append("username", username)
				append("password", password)
			}
			setBody(MultiPartFormDataContent(parts))
		}
	}
	
	@Serializable
	data class Login(
		val token: String,
	)
	
	suspend fun register(username: String, password: String): ApiResult<Unit>? {
		return ktorClient.safePost("user/register") {
			contentType(ContentType.MultiPart.FormData)
			val parts = formData {
				append("username", username)
				append("password", password)
			}
			setBody(MultiPartFormDataContent(parts))
		}
	}
}