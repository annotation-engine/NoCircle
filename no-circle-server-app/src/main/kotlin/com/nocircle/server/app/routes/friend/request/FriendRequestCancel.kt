package com.nocircle.server.app.routes.friend.request

import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.plugins.FriendRouteGroup
import com.nocircle.server.app.routes.friend.request.RequestCancelStatus.FAILURE
import com.nocircle.server.app.routes.friend.request.RequestCancelStatus.SUCCESS
import com.nocircle.server.app.tables.FriendRequests
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.Authorized
import com.nocircle.server.common.routes.getPrincipal
import com.nocircle.server.common.websockets.sendToReceiver
import com.nocircle.shared.websocket.WebSocketType
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 取消好友请求
 */
context(_: FriendRouteGroup, _: Authorized)
fun Route.postCancelRequest() = post("request/cancel") {
	val userId = call.getPrincipal().userId
	val parameters = call.receiveParameters()
	val id: Int by parameters
	val targetId: Int by parameters
	
	val success = transaction {
		FriendRequestDao.updateOne(id, userId, targetId, FriendRequests.Status.CANCELED)
	}
	val status = if (success) SUCCESS else FAILURE
	if (status == SUCCESS) {
		sendToReceiver(WebSocketType.FRIEND_RECEIVED_REQUEST, userId, targetId)
		sendToReceiver(WebSocketType.FRIEND_RECEIVED_REQUEST_COUNT, userId, targetId)
	}
	call.respondOK(status)
}

/**
 * 123x
 */
private enum class RequestCancelStatus(
	override val msg: String,
	override val code: Int
) : NoStatus {
	SUCCESS("取消成功", 0),
	FAILURE("取消失败", 1230)
}