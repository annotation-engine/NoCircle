package com.nocircle.app.pages.account.register

import com.nocircle.app.api.impls.userApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.app.resources.AppString
import com.nocircle.common.expends.isNotAlphanumeric
import com.nocircle.common.resources.getString
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class RegisterViewModel : NoViewModel() {

    val username = MutableStateFlow("")

    val password = MutableStateFlow("")

    val confirmPassword = MutableStateFlow("")

    val showPassword = MutableStateFlow(false)

    val showConfirmPassword = MutableStateFlow(false)

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

    suspend fun register(): Boolean {
        val username = this.username.value
        val password = this.password.value
        val confirmPassword = this.confirmPassword.value
        if (username.isEmpty()) {
            showNoSnackbar(AppString.REGISTER_PLEASE_INPUT_PASSWORD.getString())
            return false
        }
        if (username.length < 8) {
            showNoSnackbar(AppString.REGISTER_USERNAME_LENGTH_AT_LEAST_8.getString())
            return false
        }
        if (password.isEmpty()) {
            showNoSnackbar(AppString.REGISTER_PLEASE_INPUT_PASSWORD.getString())
            return false
        }
        if (password.length < 8) {
            showNoSnackbar(AppString.REGISTER_PASSWORD_LENGTH_AT_LEAST_8.getString())
            return false
        }
        if (password != confirmPassword) {
            showNoSnackbar(AppString.REGISTER_PASSWORD_ARE_INCONSISTENT_TWICE.getString())
            return false
        }
        val result = ktorfitx.userApi.register(username, password) ?: return networkError()
        if (!result.success) {
            showNoErrorSnackbar(result.msg)
        }
        return result.success
    }
}