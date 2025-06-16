package com.nocircle.server.app.routes.friend.request

import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.plugins.FriendContext
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.noPrincipal
import com.nocircle.server.common.model.respondDTO
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 查询未处理的消息数
 */
context(_: FriendContext)
fun Route.getQueryWaitingRequestCount() = get("request/queryWaitingCount") {
	val userId = call.noPrincipal!!.userId
	val count = transaction {
		FriendRequestDao.getReceivedWaitingRequestCount(userId)
	}
	call.respondDTO(count, RequestQueryWaitingCountStatus.SUCCESS)
}

private enum class RequestQueryWaitingCountStatus(
	override val msg: String,
	override val code: Int
) : NoStatus {
	SUCCESS("查询成功", 0)
}