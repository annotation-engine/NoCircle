package com.nocircle.server.app.routes.friend.request

import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.plugins.FriendRouteGroup
import com.nocircle.server.app.routes.friend.request.RequestQueryWaitingCountStatus.SUCCESS
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.Authorized
import com.nocircle.server.common.routes.getPrincipal
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 查询未处理的消息数
 */
context(_: FriendRouteGroup, _: Authorized)
fun Route.getQueryWaitingRequestCount() = get("request/queryWaitingCount") {
	val userId = call.getPrincipal().userId
	val count = transaction {
		FriendRequestDao.getReceivedWaitingRequestCount(userId)
	}
	call.respondOK(count, SUCCESS)
}

/**
 * 124x
 */
private enum class RequestQueryWaitingCountStatus(
	override val msg: String,
	override val code: Int
) : NoStatus {
	SUCCESS("查询成功", 0)
}