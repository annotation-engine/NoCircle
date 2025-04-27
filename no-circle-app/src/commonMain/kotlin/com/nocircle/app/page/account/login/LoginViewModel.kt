package com.nocircle.app.page.account.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class LoginViewModel : ViewModel() {
	
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
}