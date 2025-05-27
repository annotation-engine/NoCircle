package com.nocircle.server.app.plugins

import com.nocircle.server.app.services.auth.AuthVerifyTokenService
import com.nocircle.server.app.services.label.LabelAddService
import com.nocircle.server.app.services.label.LabelDeleteService
import com.nocircle.server.app.services.label.LabelQueryService
import com.nocircle.server.app.services.user.UserDetailService
import com.nocircle.server.app.services.user.UserLoginService
import com.nocircle.server.app.services.user.UserLogoutService
import com.nocircle.server.app.services.user.UserRegisterService
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
		this += LabelAddService
		this += LabelDeleteService
		this += LabelQueryService
	}
}