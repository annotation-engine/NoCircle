package com.nocircle.server.app.routes.friend.request

import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.plugins.FriendRouteGroup
import com.nocircle.server.app.routes.friend.request.RequestAddStatus.*
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
 * 添加好友请求
 */
context(_: FriendRouteGroup, _: Authorized)
fun Route.postAddRequest() = post("request/add") {
	val userId = call.getPrincipal().userId
	val parameters = call.receiveParameters()
	val targetId: Int by parameters
	if (userId == targetId) {
		return@post call.respondOK(CANNOT_ADD_ONESELF)
	}
	val status = transaction {
		val isExists = UserDao.isExistsByUserId(targetId)
		if (!isExists) return@transaction USER_NOT_FOUND
		val request = FriendRequestDao.getOneBySenderIdAndReceiverId(userId, targetId)
		if (request != null) {
			if (request.status == FriendRequests.Status.WAITING) {
				return@transaction REPEATED
			} else {
				FriendRequestDao.deleteOne(request.id.value, userId, targetId)
			}
		}
		val success = FriendRequestDao.insertOne(userId, targetId)
		if (success) SUCCESS else FAILURE
	}
	if (status == SUCCESS) {
		sendToReceiver(WebSocketType.FRIEND_RECEIVED_REQUEST, userId, targetId)
		sendToReceiver(WebSocketType.FRIEND_RECEIVED_REQUEST_COUNT, userId, targetId)
	}
	call.respondOK(status)
}

/**
 * 121x
 */
private enum class RequestAddStatus(
	override val msg: String,
	override val code: Int
) : NoStatus {
	SUCCESS("添加请求已发送", 0),
	CANNOT_ADD_ONESELF("不能添加自己为好友", 1210),
	USER_NOT_FOUND("对方用户不存在", 1211),
	REPEATED("请勿重复发送", 1212),
	FAILURE("添加请求发送失败", 1213)
}