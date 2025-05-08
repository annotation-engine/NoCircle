package com.nocircle.server.plugins

import com.nocircle.server.services.auth.AuthVerifyTokenService
import com.nocircle.server.services.user.*
import com.nocircle.server.utils.plusAssign
import com.nocircle.server.utils.services
import io.ktor.server.application.*

fun Application.configureServices() {
	services {
		this += AuthVerifyTokenService
		this += UserLoginService
		this += UserRegisterService
		this += UserLogoutService
		this += UserDetailService
		this += UserInformationService
	}
}