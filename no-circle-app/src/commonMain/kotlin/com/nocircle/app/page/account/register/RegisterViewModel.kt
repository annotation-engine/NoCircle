package com.nocircle.app.page.account.register

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import com.nocircle.app.http.ApiModel
import com.nocircle.app.http.ktorClient
import com.nocircle.compose.expends.isAlphanumeric
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.flow.MutableStateFlow

class RegisterViewModel(
	private val hostState: SnackbarHostState,
) : ViewModel() {
	
	val username = MutableStateFlow("")
	
	val password = MutableStateFlow("")
	
	val confirmPassword = MutableStateFlow("")
	
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
			hostState.showSnackbar("请输入用户名")
			return false
		}
		if (username.length < 8) {
			hostState.showSnackbar("用户名至少是8位的")
			return false
		}
		if (password.isEmpty()) {
			hostState.showSnackbar("请输入密码")
			return false
		}
		if (password.length < 8) {
			hostState.showSnackbar("密码至少是8位的")
			return false
		}
		if (password != confirmPassword) {
			hostState.showSnackbar("两次密码不一致")
			return false
		}
		
		val response = ktorClient.post("/user/register") {
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
		val model = response.body<ApiModel<Nothing>>()
		hostState.showSnackbar(model.msg)
		return model.code == 0
	}
}