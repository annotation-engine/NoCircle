package com.nocircle.app.pages.account.register

import com.nocircle.app.api.impl.userApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.app.resources.AppString
import com.nocircle.app.resources.getString
import com.nocircle.common.expends.isNotAlphanumeric
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
            showNoSnackbar(AppString.RegisterPleaseInputPassword.getString())
            return false
        }
        if (username.length < 8) {
            showNoSnackbar(AppString.RegisterUsernameLengthAtLeast8.getString())
            return false
        }
        if (password.isEmpty()) {
            showNoSnackbar(AppString.RegisterPleaseInputPassword.getString())
            return false
        }
        if (password.length < 8) {
            showNoSnackbar(AppString.RegisterPasswordLengthAtLeast8.getString())
            return false
        }
        if (password != confirmPassword) {
            showNoSnackbar(AppString.RegisterPasswordsAreInconsistentTwice.getString())
            return false
        }
        val result = ktorfitx.userApi.register(username, password) ?: return networkError()
        if (!result.success) {
            showNoErrorSnackbar(result.msg)
        }
        return result.success
    }
}