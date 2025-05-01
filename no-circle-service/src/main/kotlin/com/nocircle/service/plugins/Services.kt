package com.nocircle.service.plugins

import com.nocircle.service.services.user.UserLoginService
import com.nocircle.service.services.user.UserLogoutService
import com.nocircle.service.services.user.UserRegisterService
import com.nocircle.service.utils.plusAssign
import com.nocircle.service.utils.services
import io.ktor.server.application.*

fun Application.configureServices() {
	services {
		this += UserLoginService
		this += UserRegisterService
		this += UserLogoutService
	}
}