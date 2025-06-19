package com.nocircle.app.pages.account.register

import com.nocircle.app.api.impls.userApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.app.resources.AppString
import com.nocircle.common.coroutines.FunctionLocker
import com.nocircle.common.coroutines.OnBusyReturnFalse
import com.nocircle.common.expends.isNotAlphanumeric
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class RegisterViewModel : NoViewModel() {
	
	val username = MutableStateFlow("")
	
	val password = MutableStateFlow("")
	
	val confirmPassword = MutableStateFlow("")
	
	val showPassword = MutableStateFlow(false)
	
	val showConfirmPassword = MutableStateFlow(false)
	
	val nickname = MutableStateFlow("")
	
	fun updateUsername(value: String) {
		if (value.length > 20 || value.isNotAlphanumeric()) return
		this.username.value = value
	}
	
	fun updatePassword(value: String) {
		if (value.length > 20) return
		this.password.value = value
	}
	
	fun updateConfirmPassword(value: String) {
		if (value.length > 20) return
		this.confirmPassword.value = value
	}
	
	fun updateNickname(value: String) {
		if (value.length > 20) return
		this.nickname.value = value
	}
	
	suspend fun register(): Boolean {
		return FunctionLocker.tryWithLock(::register, OnBusyReturnFalse) {
			val username = this.username.value
			val password = this.password.value
			val nickname = this.nickname.value
			val confirmPassword = this.confirmPassword.value
			if (username.isEmpty()) {
				showNoSnackbar(AppString.REGISTER_PLEASE_INPUT_PASSWORD)
				return@tryWithLock false
			}
			if (username.length < 8) {
				showNoSnackbar(AppString.REGISTER_USERNAME_LENGTH_AT_LEAST_8)
				return@tryWithLock false
			}
			if (password.isEmpty()) {
				showNoSnackbar(AppString.REGISTER_PLEASE_INPUT_PASSWORD)
				return@tryWithLock false
			}
			if (password.length < 8) {
				showNoSnackbar(AppString.REGISTER_PASSWORD_LENGTH_AT_LEAST_8)
				return@tryWithLock false
			}
			if (password != confirmPassword) {
				showNoSnackbar(AppString.REGISTER_PASSWORD_ARE_INCONSISTENT_TWICE)
				return@tryWithLock false
			}
			if (nickname.isBlank()) {
				showNoSnackbar(AppString.REGISTER_NICKNAME_NOT_BLANK)
				return@tryWithLock false
			}
			val result = ktorfitx.userApi.register(username, password, nickname)
				?: return@tryWithLock networkError()
			if (!result.success) {
				showNoErrorSnackbar(result.msg)
			}
			result.success
		}
	}
}