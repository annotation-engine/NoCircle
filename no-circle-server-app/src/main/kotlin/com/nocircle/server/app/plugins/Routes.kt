package com.nocircle.server.app.plugins

import com.nocircle.server.app.routes.auth.verifyToken
import com.nocircle.server.app.routes.friend.queryFriend
import com.nocircle.server.app.routes.friend.queryFriendVersion
import com.nocircle.server.app.routes.friend.request.*
import com.nocircle.server.app.routes.label.addLabel
import com.nocircle.server.app.routes.label.deleteLabel
import com.nocircle.server.app.routes.label.queryLabel
import com.nocircle.server.app.routes.label.updateLabel
import com.nocircle.server.app.routes.user.*
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
		verifyToken()
	}
}

object UserRouteGroup : NoRouteGroup("user") {
	
	override fun Route.routes() {
		userLogin()
		userRegister()
	}
	
	context(_: Authorized)
	override fun Route.authenticates() {
		userDetail()
		userLogout()
		searchUser()
	}
}

object LabelRouteGroup : NoRouteGroup("label") {
	
	context(_: Authorized)
	override fun Route.authenticates() {
		addLabel()
		deleteLabel()
		updateLabel()
		queryLabel()
	}
}

object FriendRouteGroup : NoRouteGroup("friend") {
	
	context(_: Authorized)
	override fun Route.authenticates() {
		queryFriend()
		queryFriendVersion()
		
		addFriendRequest()
		queryFriendRequest()
		cancelFriendRequest()
		deleteFriendRequest()
		queryFriendRequestPendingCount()
		rejectFriendRequest()
		agreeFriendRequest()
	}
}