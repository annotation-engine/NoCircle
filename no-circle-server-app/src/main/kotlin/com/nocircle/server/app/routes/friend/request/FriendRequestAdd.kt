package com.nocircle.server.app.routes.friend.request

import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.plugins.FriendRouteGroup
import com.nocircle.server.app.tables.FriendRequests
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
 * 添加好友请求
 */
context(_: FriendRouteGroup, _: Authorized)
fun Route.postAddRequest() = post("request/add") {
	val userId = call.getPrincipal().userId
	val parameters = call.receiveParameters()
	val targetId: Int by parameters
	if (userId == targetId) {
		return@post call.respondOK(NoCode.FRIEND_REQUEST_ADD_CANNOT_ADD_ONESELF)
	}
	val code = transaction {
		val isExists = UserDao.isExistsByUserId(targetId)
		if (!isExists) return@transaction NoCode.FRIEND_REQUEST_ADD_USER_NOT_FOUND
		val request = FriendRequestDao.getOneBySenderIdAndReceiverId(userId, targetId)
		if (request != null) {
			if (request.status == FriendRequests.Status.WAITING) {
				return@transaction NoCode.FRIEND_REQUEST_ADD_REPEATED
			} else {
				FriendRequestDao.deleteOne(request.id.value, userId, targetId)
			}
		}
		val success = FriendRequestDao.insertOne(userId, targetId)
		if (success) NoCode.FRIEND_REQUEST_ADD_SUCCESS else NoCode.FRIEND_REQUEST_ADD_FAILURE
	}
	if (code == NoCode.FRIEND_REQUEST_ADD_SUCCESS) {
		sendToReceiver(WebSocketType.FRIEND_RECEIVED_REQUEST, userId, targetId)
		sendToReceiver(WebSocketType.FRIEND_RECEIVED_REQUEST_COUNT, userId, targetId)
	}
	call.respondOK(code)
}