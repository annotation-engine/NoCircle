package com.nocircle.app.page.account.register

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import com.nocircle.app.generated.resources.*
import com.nocircle.app.http.ktorClient
import com.nocircle.common.expends.isAlphanumeric
import com.nocircle.common.expends.safePost
import com.nocircle.compose.material3.NoSnackbarColors
import com.nocircle.compose.material3.showNoSnackbar
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.flow.MutableStateFlow

class RegisterViewModel(
	private val hostState: SnackbarHostState,
) : ViewModel() {
	
	val username = MutableStateFlow("lijiawei")
	
	val password = MutableStateFlow("12345678")
	
	val confirmPassword = MutableStateFlow("12345678")
	
	val showPassword = MutableStateFlow(false)
	
	val showConfirmPassword = MutableStateFlow(false)
	
	fun updateUsername(value: String) {
		if (value.length <= 20 && value.isAlphanumeric()) {
			this.username.value = value
		}
	}
	
	fun updatePassword(value: String) {
		if (value.length <= 20) {
			this.password.value = value
		}
	}
	
	fun updateConfirmPassword(value: String) {
		if (value.length <= 20) {
			this.confirmPassword.value = value
		}
	}
	
	suspend fun register(): Boolean {
		val username = this.username.value
		val password = this.password.value
		val confirmPassword = this.confirmPassword.value
		if (username.isEmpty()) {
			hostState.showNoSnackbar(Res.string.register_please_input_username)
			return false
		}
		if (username.length < 8) {
			hostState.showNoSnackbar(Res.string.register_username_length_at_least_8)
			return false
		}
		if (password.isEmpty()) {
			hostState.showNoSnackbar(Res.string.register_please_input_password)
			return false
		}
		if (password.length < 8) {
			hostState.showNoSnackbar(Res.string.register_password_length_at_least_8)
			return false
		}
		if (password != confirmPassword) {
			hostState.showNoSnackbar(Res.string.register_passwords_are_inconsistent_twice)
			return false
		}
		
		val result = ktorClient.safePost<Unit>("/user/register") {
			contentType(ContentType.MultiPart.FormData)
			val parts = formData {
				append("username", username)
				append("password", password)
			}
			setBody(MultiPartFormDataContent(parts))
		}
		if (result == null) {
			hostState.showNoSnackbar(Res.string.global_network_connection_error, colors = NoSnackbarColors.Error)
			return false
		}
		if (result.failure) {
			hostState.showNoSnackbar(result.msg)
		}
		return result.success
	}
}