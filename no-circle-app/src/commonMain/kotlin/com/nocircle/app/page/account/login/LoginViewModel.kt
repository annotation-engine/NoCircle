package com.nocircle.app.page.account.login

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import com.nocircle.app.generated.resources.Res
import com.nocircle.app.generated.resources.login_please_input_password
import com.nocircle.app.generated.resources.login_please_input_username
import com.nocircle.app.http.ApiResult
import com.nocircle.app.http.ktorClient
import com.nocircle.common.expends.value
import com.nocircle.compose.material3.showNoSnackbar
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable

class LoginViewModel(
	private val hostState: SnackbarHostState,
) : ViewModel() {
	
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
			hostState.showNoSnackbar(Res.string.login_please_input_username.value())
			return false
		}
		if (password.isEmpty()) {
			hostState.showNoSnackbar(Res.string.login_please_input_password.value())
			return false
		}
		val response = ktorClient.post("user/login") {
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
		val model = response.body<ApiResult<Login>>()
		if (model.success) {
			// 保存 token
		} else {
			hostState.showNoSnackbar(model.msg)
		}
		return model.success
	}
	
	@Serializable
	private data class Login(
		val token: String,
	)
}