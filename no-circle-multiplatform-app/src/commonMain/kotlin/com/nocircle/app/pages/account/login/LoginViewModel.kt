package com.nocircle.app.pages.account.login

import com.nocircle.app.generated.resources.Res
import com.nocircle.app.generated.resources.global_network_connect_error
import com.nocircle.app.generated.resources.login_please_input_password
import com.nocircle.app.generated.resources.login_please_input_username
import com.nocircle.app.http.ktorClient
import com.nocircle.common.config.setConfig
import com.nocircle.common.ktor.safePost
import com.nocircle.compose.viewmodel.NoViewModel
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class LoginViewModel() : NoViewModel() {
	
	private val _username = MutableStateFlow("")
	val username = _username.asStateFlow()
	
	private val _password = MutableStateFlow("")
	val password = _password.asStateFlow()
	
	val showPassword = MutableStateFlow(false)
	
	fun updateUsername(value: String) {
		if (value.length <= 20) {
			this._username.value = value
		}
	}
	
	fun updatePassword(value: String) {
		if (value.length <= 20) {
			this._password.value = value
		}
	}
	
	suspend fun login(): Boolean {
		val username = this._username.value
		val password = this._password.value
		if (username.isEmpty()) {
			showNoSnackbar(Res.string.login_please_input_username)
			return false
		}
		if (password.isEmpty()) {
			showNoSnackbar(Res.string.login_please_input_password)
			return false
		}
		val result = ktorClient.safePost<Login>("user/login", auth = false) {
			contentType(ContentType.MultiPart.FormData)
			val parameters = parameters {
				append("username", username)
				append("password", password)
			}
			setBody(FormDataContent(parameters))
		} ?: let {
			showNoErrorSnackbar(Res.string.global_network_connect_error)
			return false
		}
		if (result.success) {
			setConfig("token", result.data!!.token)
		} else {
			showNoErrorSnackbar(result.msg)
		}
		return result.success
	}
	
	@Serializable
	data class Login(
		val token: String,
	)
}