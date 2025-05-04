package com.nocircle.app.pages.account.login

import com.nocircle.app.generated.resources.Res
import com.nocircle.app.generated.resources.login_please_input_password
import com.nocircle.app.generated.resources.login_please_input_username
import com.nocircle.app.http.ktorClient
import com.nocircle.app.utils.ConfigUtils
import com.nocircle.common.expends.safePost
import com.nocircle.common.viewmodel.NoViewModel
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable

class LoginViewModel() : NoViewModel() {
	
	val username = MutableStateFlow("")
	val password = MutableStateFlow("")
	val showPassword = MutableStateFlow(false)
	
	fun updateUsername(value: String) {
		if (value.length <= 20) {
			this.username.value = value
		}
	}
	
	fun updatePassword(value: String) {
		if (value.length <= 20) {
			this.password.value = value
		}
	}
	
	suspend fun login(): Boolean {
		val username = this.username.value
		val password = this.password.value
		if (username.isEmpty()) {
			showNoSnackbar(Res.string.login_please_input_username)
			return false
		}
		if (password.isEmpty()) {
			showNoSnackbar(Res.string.login_please_input_password)
			return false
		}
		val result = ktorClient.safePost<Login>("user/login") {
			contentType(ContentType.MultiPart.FormData)
			val parts = formData {
				append("username", username)
				append("password", password)
			}
			setBody(MultiPartFormDataContent(parts))
		} ?: return false
		if (result.success) {
			ConfigUtils.setValue("token", result.data!!.token)
		}
		return result.success
	}
	
	@Serializable
	data class Login(
		val token: String,
	)
}