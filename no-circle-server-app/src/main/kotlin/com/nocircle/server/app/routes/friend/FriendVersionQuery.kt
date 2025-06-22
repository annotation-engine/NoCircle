package com.nocircle.server.app.routes.friend

import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendVersionDao
import com.nocircle.server.app.plugins.FriendRouteGroup
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.Authorized
import com.nocircle.server.common.routes.getPrincipal
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

context(_: FriendRouteGroup, _: Authorized)
fun Route.queryFriendVersion() = get("queryVersion") {
	val userId = call.getPrincipal().userId
	val version = transaction {
		FriendVersionDao.getVersionByUserId(userId) ?: 0
	}
	call.respondOK(version, NoCode.FRIEND_VERSION_QUERY_SUCCESS)
}