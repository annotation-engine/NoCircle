package com.nocircle.server.app.routes.friend

import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendVersionDao
import com.nocircle.server.app.plugins.FriendRouteContext
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.AuthContext
import com.nocircle.server.common.routes.getPrincipal
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

context(_: FriendRouteContext, _: AuthContext)
fun Route.queryFriendVersion() = get("version/query") {
	val userId = call.getPrincipal().userId
	val version = transaction {
		FriendVersionDao.getVersionByUserId(userId) ?: 0
	}
	call.respondOK(version, NoCode.FRIEND_VERSION_QUERY_SUCCESS)
}