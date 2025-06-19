package com.nocircle.server.app.plugins

import com.nocircle.server.app.routes.auth.postVerifyToken
import com.nocircle.server.app.routes.friend.getQuery
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
import com.nocircle.server.common.routes.Authorized
import com.nocircle.server.common.routes.NoRouteGroup
import com.nocircle.server.common.routes.routeContexts
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRoutes() {
	routeContexts(
		AuthRouteGroup,
		UserRouteGroup,
		LabelRouteGroup,
		FriendRouteGroup
	)
}

object AuthRouteGroup : NoRouteGroup("auth") {
	
	context(_: Authorized)
	override fun Route.authenticates() {
		postVerifyToken()
	}
}

object UserRouteGroup : NoRouteGroup("user") {
	
	override fun Route.routes() {
		postUserLogin()
		postUserRegister()
	}
	
	context(_: Authorized)
	override fun Route.authenticates() {
		getUserDetail()
		postUserLogout()
	}
}

object LabelRouteGroup : NoRouteGroup("label") {
	
	context(_: Authorized)
	override fun Route.authenticates() {
		postAddLabel()
		postDeleteLabel()
		postUpdateLabel()
		getQueryLabel()
	}
}

object FriendRouteGroup : NoRouteGroup("friend") {
	
	context(_: Authorized)
	override fun Route.authenticates() {
		getSearch()
		getQuery()
		postAddRequest()
		getQueryRequest()
		postCancelRequest()
		postDeleteRequest()
		getQueryWaitingRequestCount()
		postRejectRequest()
		postAgreeRequest()
	}
}