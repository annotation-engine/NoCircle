package com.nocircle.server.app.routes.friend.request

import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.plugins.FriendRouteGroup
import com.nocircle.server.app.plugins.WebSocketType
import com.nocircle.server.app.routes.friend.request.RequestRejectStatus.FAILURE
import com.nocircle.server.app.routes.friend.request.RequestRejectStatus.SUCCESS
import com.nocircle.server.app.tables.FriendRequests
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.Authorized
import com.nocircle.server.common.routes.getPrincipal
import com.nocircle.server.common.websockets.getSession
import com.nocircle.server.common.websockets.sendMessage
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 拒绝好友请求
 */
context(_: FriendRouteGroup, _: Authorized)
fun Route.postRejectRequest() = post("request/reject") {
	val userId = call.getPrincipal().userId
	val parameters = call.receiveParameters()
	val id: Int by parameters
	val targetId: Int by parameters
	val success = transaction {
		FriendRequestDao.updateOne(id, targetId, userId, FriendRequests.Status.REJECTED)
	}
	val status = if (success) SUCCESS else FAILURE
	if (status == SUCCESS) {
		getSession(targetId)?.sendMessage(
			type = WebSocketType.REFRESH_FRIEND_SENT_REQUEST,
			senderId = userId
		)
	}
	call.respondOK(status)
}

/**
 * 125x
 */
private enum class RequestRejectStatus(
	override val msg: String,
	override val code: Int
) : NoStatus {
	SUCCESS("拒绝成功", 0),
	FAILURE("拒绝失败", 1250)
}