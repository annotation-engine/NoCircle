package com.nocircle.app.pages.account.login

import com.nocircle.app.api.impl.userApi
import com.nocircle.app.generated.resources.Res
import com.nocircle.app.generated.resources.global_network_connect_error
import com.nocircle.app.generated.resources.login_please_input_password
import com.nocircle.app.generated.resources.login_please_input_username
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.common.config.TokenConfigKey
import com.nocircle.common.config.set
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable

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
		val result = ktorfitx.userApi.login(username, password) ?: let {
			showNoErrorSnackbar(Res.string.global_network_connect_error)
			return false
		}
		if (result.success) {
			TokenConfigKey.set(result.data!!.token)
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