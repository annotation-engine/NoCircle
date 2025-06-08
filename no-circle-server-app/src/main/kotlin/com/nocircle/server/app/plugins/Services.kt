package com.nocircle.server.app.plugins

import com.nocircle.server.app.services.auth.AuthVerifyTokenService
import com.nocircle.server.app.services.label.LabelAddService
import com.nocircle.server.app.services.label.LabelDeleteService
import com.nocircle.server.app.services.label.LabelQueryService
import com.nocircle.server.app.services.label.LabelUpdateService
import com.nocircle.server.app.services.user.*
import com.nocircle.server.common.utils.group
import com.nocircle.server.common.utils.plusAssign
import com.nocircle.server.common.utils.services
import io.ktor.server.application.*

fun Application.configureServices() {
	services {
		group {
			this += AuthVerifyTokenService
		}
		group {
			this += UserLoginService
			this += UserRegisterService
			this += UserLogoutService
			this += UserDetailService
			this += UserQueryService
		}
		group {
			this += LabelAddService
			this += LabelDeleteService
			this += LabelUpdateService
			this += LabelQueryService
		}
	}
}