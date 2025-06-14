package com.nocircle.server.app.plugins

import com.nocircle.server.app.routes.auth.postVerifyToken
import com.nocircle.server.app.routes.friend.getSearch
import com.nocircle.server.app.routes.friend.request.getRequestQuery
import com.nocircle.server.app.routes.friend.request.postRequestAdd
import com.nocircle.server.app.routes.label.getQuery
import com.nocircle.server.app.routes.label.postAdd
import com.nocircle.server.app.routes.label.postDelete
import com.nocircle.server.app.routes.label.postUpdate
import com.nocircle.server.app.routes.user.getDetail
import com.nocircle.server.app.routes.user.postLogin
import com.nocircle.server.app.routes.user.postLogout
import com.nocircle.server.app.routes.user.postRegister
import com.nocircle.server.common.routes.RouteContext
import com.nocircle.server.common.routes.routeContexts
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRoutes() {
	routeContexts(
		AuthContext,
		UserContext,
		LabelContext,
		FriendContext
	)
}

object AuthContext : RouteContext("auth") {
	
	override fun Route.authenticates() {
		postVerifyToken()
	}
}

object UserContext : RouteContext("user") {
	
	override fun Route.routes() {
		postLogin()
		postRegister()
	}
	
	override fun Route.authenticates() {
		getDetail()
		postLogout()
	}
}

object LabelContext : RouteContext("label") {
	
	override fun Route.authenticates() {
		postAdd()
		postDelete()
		postUpdate()
		getQuery()
	}
}

object FriendContext : RouteContext("friend") {
	
	override fun Route.authenticates() {
		getSearch()
		postRequestAdd()
		getRequestQuery()
	}
}