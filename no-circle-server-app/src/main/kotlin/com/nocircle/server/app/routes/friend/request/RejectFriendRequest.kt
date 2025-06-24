package com.nocircle.server.app.routes.friend.request

import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.plugins.FriendRouteContext
import com.nocircle.server.app.tables.FriendRequests
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.AuthContext
import com.nocircle.server.common.routes.getPrincipal
import com.nocircle.server.common.websockets.sendToReceiver
import com.nocircle.shared.websocket.WebSocketType
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 拒绝好友请求
 */
context(_: FriendRouteContext, _: AuthContext)
fun Route.rejectFriendRequest() = post("request/reject") {
	val userId = call.getPrincipal().userId
	val parameters = call.receiveParameters()
	val id: Int by parameters
	val targetId: Int by parameters
	val success = transaction {
		FriendRequestDao.updateOne(id, targetId, userId, FriendRequests.Status.REJECTED)
	}
	if (success) {
		sendToReceiver(WebSocketType.FRIEND_SENT_REQUEST, userId, targetId)
		sendToReceiver(WebSocketType.FRIEND_RECEIVED_REQUEST_COUNT, userId, userId)
	}
	val code = if (success) NoCode.FRIEND_REQUEST_REJECT_SUCCESS else NoCode.FRIEND_REQUEST_REJECT_FAILURE
	call.respondOK(code)
}