package com.nocircle.server.app.plugins

import com.nocircle.server.app.services.auth.AuthVerifyTokenService
import com.nocircle.server.app.services.user.*
import com.nocircle.server.common.utils.plusAssign
import com.nocircle.server.common.utils.services
import io.ktor.server.application.*

fun Application.configureServices() {
	services {
		this += AuthVerifyTokenService
		this += UserLoginService
		this += UserRegisterService
		this += UserLogoutService
		this += UserDetailService
	}
}