package com.nocircle.server.app.routes.friend.relationship

import com.nocircle.server.app.dao.FriendRelationshipDao
import com.nocircle.server.app.plugins.FriendRouteGroup
import com.nocircle.server.common.routes.Authorized
import com.nocircle.server.common.routes.getPrincipal
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

context(_: FriendRouteGroup, _: Authorized)
fun Route.getQueryRelationship() = get("relationship/query") {
	val userId = call.getPrincipal().userId
	val parameters = call.parameters
	val page: Int by parameters
	val size: Int by parameters
	transaction {
		FriendRelationshipDao.queryList(userId, page, size)
	}
}