package com.nocircle.server.app.plugins

import com.nocircle.server.app.routes.auth.AuthVerifyTokenRoute
import com.nocircle.server.app.routes.label.LabelAddRoute
import com.nocircle.server.app.routes.label.LabelDeleteRoute
import com.nocircle.server.app.routes.label.LabelQueryRoute
import com.nocircle.server.app.routes.label.LabelUpdateRoute
import com.nocircle.server.app.routes.user.*
import com.nocircle.server.common.route.group
import com.nocircle.server.common.route.plusAssign
import com.nocircle.server.common.route.routes
import io.ktor.server.application.*

fun Application.configureRoutes() {
	routes {
		group {
			this += AuthVerifyTokenRoute
		}
		group {
			this += UserLoginRoute
			this += UserRegisterRoute
			this += UserLogoutRoute
			this += UserDetailRoute
			this += UserQueryRoute
		}
		group {
			this += LabelAddRoute
			this += LabelDeleteRoute
			this += LabelUpdateRoute
			this += LabelQueryRoute
		}
	}
}