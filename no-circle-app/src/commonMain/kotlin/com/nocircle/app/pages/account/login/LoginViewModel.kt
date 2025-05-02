package com.nocircle.app.pages.account.login

import com.nocircle.app.generated.resources.Res
import com.nocircle.app.generated.resources.global_network_connection_error
import com.nocircle.app.generated.resources.login_please_input_password
import com.nocircle.app.generated.resources.login_please_input_username
import com.nocircle.app.http.api.UserApi
import com.nocircle.app.utils.ConfigUtils
import com.nocircle.common.material3.NoSnackbarColors
import com.nocircle.common.viewmodel.NoViewModel
import kotlinx.coroutines.flow.MutableStateFlow

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
		val result = UserApi.login(username, password)
		if (result == null) {
			showNoSnackbar(Res.string.global_network_connection_error, colors = NoSnackbarColors.Error)
			return false
		}
		if (result.success) {
			ConfigUtils.setValue("token", result.data!!.token)
		} else {
			showNoSnackbar(result.msg, colors = NoSnackbarColors.Error)
		}
		return result.success
	}
}