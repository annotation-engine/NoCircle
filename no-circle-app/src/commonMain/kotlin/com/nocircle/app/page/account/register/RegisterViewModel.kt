package com.nocircle.app.page.account.register

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import com.nocircle.app.generated.resources.*
import com.nocircle.app.http.ApiResult
import com.nocircle.app.http.ktorClient
import com.nocircle.common.expends.isAlphanumeric
import com.nocircle.common.expends.value
import com.nocircle.compose.material3.showNoSnackbar
import io.ktor.client.call.*
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
			hostState.showNoSnackbar(Res.string.register_please_input_username.value())
			return false
		}
		if (username.length < 8) {
			hostState.showNoSnackbar(Res.string.register_username_length_at_least_8.value())
			return false
		}
		if (password.isEmpty()) {
			hostState.showNoSnackbar(Res.string.register_please_input_password.value())
			return false
		}
		if (password.length < 8) {
			hostState.showNoSnackbar(Res.string.register_password_length_at_least_8.value())
			return false
		}
		if (password != confirmPassword) {
			hostState.showNoSnackbar(Res.string.register_passwords_are_inconsistent_twice.value())
			return false
		}
		
		val response = ktorClient.post("user/register") {
			contentType(ContentType.MultiPart.FormData)
			val parts = formData {
				append("username", username)
				append("password", password)
			}
			setBody(MultiPartFormDataContent(parts))
		}
		if (response.status != HttpStatusCode.OK) {
			return false
		}
		val model = response.body<ApiResult<Nothing>>()
		if (!model.success) {
			hostState.showNoSnackbar(model.msg)
		}
		return model.success
	}
}