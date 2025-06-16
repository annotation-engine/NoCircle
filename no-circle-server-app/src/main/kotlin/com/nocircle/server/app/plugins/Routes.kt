package com.nocircle.server.app.plugins

import com.nocircle.server.app.routes.auth.postVerifyToken
import com.nocircle.server.app.routes.friend.getSearch
import com.nocircle.server.app.routes.friend.request.*
import com.nocircle.server.app.routes.label.getQueryLabel
import com.nocircle.server.app.routes.label.postAddLabel
import com.nocircle.server.app.routes.label.postDeleteLabel
import com.nocircle.server.app.routes.label.postUpdateLabel
import com.nocircle.server.app.routes.user.getUserDetail
import com.nocircle.server.app.routes.user.postUserLogin
import com.nocircle.server.app.routes.user.postUserLogout
import com.nocircle.server.app.routes.user.postUserRegister
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
		postUserLogin()
		postUserRegister()
	}
	
	override fun Route.authenticates() {
		getUserDetail()
		postUserLogout()
	}
}

object LabelContext : RouteContext("label") {
	
	override fun Route.authenticates() {
		postAddLabel()
		postDeleteLabel()
		postUpdateLabel()
		getQueryLabel()
	}
}

object FriendContext : RouteContext("friend") {
	
	override fun Route.authenticates() {
		getSearch()
		postAddRequest()
		getQueryRequest()
		postCancelRequest()
		postDeleteRequest()
		getQueryReceivedWaitingRequestCount()
	}
}