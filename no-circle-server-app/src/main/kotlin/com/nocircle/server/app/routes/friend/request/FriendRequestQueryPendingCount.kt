package com.nocircle.server.app.routes.friend.request

import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.plugins.FriendRouteGroup
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.Authorized
import com.nocircle.server.common.routes.getPrincipal
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 查询未处理的消息数
 */
context(_: FriendRouteGroup, _: Authorized)
fun Route.queryFriendRequestPendingCount() = get("request/queryPendingCount") {
	val userId = call.getPrincipal().userId
	val count = transaction {
		FriendRequestDao.getReceivedPendingRequestCount(userId)
	}
	call.respondOK(count, NoCode.FRIEND_REQUEST_QUERY_PENDING_COUNT_SUCCESS)
}