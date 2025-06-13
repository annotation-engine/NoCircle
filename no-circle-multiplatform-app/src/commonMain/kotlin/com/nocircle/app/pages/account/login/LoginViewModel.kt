package com.nocircle.app.pages.account.login

import com.nocircle.app.api.impls.userApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.app.resources.AppString
import com.nocircle.common.config.TokenConfigKey
import com.nocircle.common.config.set
import com.nocircle.common.resources.getString
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel() : NoViewModel() {
	
	private val _username = MutableStateFlow("")
	val username = _username.asStateFlow()
	
	private val _password = MutableStateFlow("")
	val password = _password.asStateFlow()
	
	val showPassword = MutableStateFlow(false)
	
	fun updateUsername(value: String) {
		if (value.length > 20) return
		this._username.value = value
	}
	
	fun updatePassword(value: String) {
		if (value.length > 20) return
		this._password.value = value
	}
	
	suspend fun login(): Boolean {
		val username = this._username.value
		val password = this._password.value
		if (username.isEmpty()) {
			showNoSnackbar(AppString.LOGIN_PLEASE_INPUT_USERNAME.getString())
			return false
		}
		if (password.isEmpty()) {
			showNoSnackbar(AppString.LOGIN_PLEASE_INPUT_PASSWORD.getString())
			return false
		}
		val result = ktorfitx.userApi.login(username, password) ?: return networkError()
		if (result.success) {
			TokenConfigKey.set(result.data!!.token)
		} else {
			showNoErrorSnackbar(result.msg)
		}
		return result.success
	}
}