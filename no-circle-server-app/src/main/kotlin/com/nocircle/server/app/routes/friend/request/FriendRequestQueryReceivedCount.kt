package com.nocircle.server.app.routes.friend.request

import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.plugins.FriendContext
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.noPrincipal
import com.nocircle.server.common.model.respondDTO
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

context(_: FriendContext)
fun Route.getQueryReceivedWaitingRequestCount() = get("request/queryReceivedWaitingCount") {
	val userId = call.noPrincipal!!.userId
	val count = transaction {
		FriendRequestDao.getReceivedWaitingRequestCount(userId)
	}
	call.respondDTO(count, RequestQueryReceivedCountStatus.SUCCESS)
}

private enum class RequestQueryReceivedCountStatus(
	override val msg: String,
	override val code: Int
) : NoStatus {
	SUCCESS("查询成功", 0)
}