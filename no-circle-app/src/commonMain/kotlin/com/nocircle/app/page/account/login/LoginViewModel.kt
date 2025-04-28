package com.nocircle.app.page.account.login

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import com.nocircle.app.http.ApiModel
import com.nocircle.app.http.ktorClient
import com.nocircle.compose.material3.showNoSnackbar
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
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
			hostState.showNoSnackbar(
				message = "请输入用户名请输入用户名请输入用户名请输入用户名请输入用户名请输入用户名请输入用户名请输入用户名请输入用户名请输入用户名",
				actionLabel = "确定",
				prefixIcon = Icons.Default.Warning,
				withDismissAction = true,
				duration = SnackbarDuration.Indefinite
			)
			return false
		}
		if (password.isEmpty()) {
			hostState.showSnackbar("请输入密码")
			return false
		}
		val response = ktorClient.post("/user/login") {
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
		val model = response.body<ApiModel<Login>>()
		hostState.showSnackbar(model.msg)
		if (model.success) {
			// 储存 token
		}
		return model.success
	}
	
	@Serializable
	private data class Login(
		val token: String,
	)
}