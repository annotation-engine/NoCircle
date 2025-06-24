package com.nocircle.server.app.plugins

import com.nocircle.server.app.routes.auth.verifyToken
import com.nocircle.server.app.routes.friend.queryFriend
import com.nocircle.server.app.routes.friend.queryFriendDetail
import com.nocircle.server.app.routes.friend.queryFriendVersion
import com.nocircle.server.app.routes.friend.request.*
import com.nocircle.server.app.routes.label.addLabel
import com.nocircle.server.app.routes.label.deleteLabel
import com.nocircle.server.app.routes.label.queryLabel
import com.nocircle.server.app.routes.label.updateLabel
import com.nocircle.server.app.routes.user.*
import com.nocircle.server.common.routes.AuthContext
import com.nocircle.server.common.routes.RouteContext
import com.nocircle.server.common.routes.routes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRoutes() {
	routes(
		AuthRouteContext,
		UserRouteContext,
		LabelRouteContext,
		FriendRouteContext
	)
}

object AuthRouteContext : RouteContext("auth") {
	
	context(_: AuthContext)
	override fun Route.authenticates() {
		verifyToken()
	}
}

object UserRouteContext : RouteContext("user") {
	
	override fun Route.routes() {
		userLogin()
		userRegister()
	}
	
	context(_: AuthContext)
	override fun Route.authenticates() {
		userDetail()
		userLogout()
		searchUser()
	}
}

object LabelRouteContext : RouteContext("label") {
	
	context(_: AuthContext)
	override fun Route.authenticates() {
		addLabel()
		deleteLabel()
		updateLabel()
		queryLabel()
	}
}

object FriendRouteContext : RouteContext("friend") {
	
	context(_: AuthContext)
	override fun Route.authenticates() {
		queryFriend()
		queryFriendVersion()
		queryFriendDetail()
		
		addFriendRequest()
		queryFriendRequest()
		cancelFriendRequest()
		deleteFriendRequest()
		queryFriendRequestPendingCount()
		rejectFriendRequest()
		agreeFriendRequest()
	}
}