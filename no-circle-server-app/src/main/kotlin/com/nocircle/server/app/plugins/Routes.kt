package com.nocircle.server.app.plugins

import com.nocircle.server.app.routes.auth.postVerifyToken
import com.nocircle.server.app.routes.friend.getSearch
import com.nocircle.server.app.routes.friend.postAddRequest
import com.nocircle.server.app.routes.label.getQuery
import com.nocircle.server.app.routes.label.postAdd
import com.nocircle.server.app.routes.label.postDelete
import com.nocircle.server.app.routes.label.postUpdate
import com.nocircle.server.app.routes.user.getDetail
import com.nocircle.server.app.routes.user.postLogin
import com.nocircle.server.app.routes.user.postLogout
import com.nocircle.server.app.routes.user.postRegister
import com.nocircle.server.common.routes.RouteContext
import com.nocircle.server.common.routes.authenticateRoute
import com.nocircle.server.common.routes.route
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRoutes() {
	routing {
		route<UserContext> {
			postLogin()
			postRegister()
		}
		authenticateRoute<AuthContext> {
			postVerifyToken()
		}
		authenticateRoute<UserContext> {
			getDetail()
			postLogout()
		}
		authenticateRoute<LabelContext> {
			postAdd()
			postDelete()
			postUpdate()
			getQuery()
		}
		authenticateRoute<FriendContext> {
			getSearch()
			postAddRequest()
		}
	}
}

object AuthContext : RouteContext {
	override val path = "auth"
}

object UserContext : RouteContext {
	override val path = "user"
}

object LabelContext : RouteContext {
	override val path = "label"
}

object FriendContext : RouteContext {
	override val path = "friend"
}