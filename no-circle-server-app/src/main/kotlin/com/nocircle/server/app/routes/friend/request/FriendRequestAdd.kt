package com.nocircle.server.app.routes.friend.request

import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.plugins.FriendContext
import com.nocircle.server.app.plugins.WebSocketType
import com.nocircle.server.app.tables.FriendRequests
import com.nocircle.server.common.exposed.getInt
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.noPrincipal
import com.nocircle.server.common.model.respondDTO
import com.nocircle.server.common.websockets.getSession
import com.nocircle.server.common.websockets.sendMessage
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 添加好友请求
 */
context(_: FriendContext)
fun Route.postAddRequest() = post("request/add") {
	val userId = call.noPrincipal!!.userId
	val receiverId = call.receiveParameters().getInt("receiverId")
	if (userId == receiverId) {
		return@post call.respondDTO(RequestAddStatus.CANNOT_ADD_ONESELF)
	}
	val status = transaction {
		val isExists = UserDao.isExistsByUserId(receiverId)
		if (!isExists) return@transaction RequestAddStatus.USER_NOT_FOUND
		val request = FriendRequestDao.getOneBySenderIdAndReceiverId(userId, receiverId)
		if (request != null) {
			if (request.status == FriendRequests.Status.WAITING) {
				return@transaction RequestAddStatus.REPEATED
			} else {
				FriendRequestDao.deleteByIdAndSenderId(request.id.value, request.senderId)
			}
		}
		val success = FriendRequestDao.insertOne(userId, receiverId)
		if (success) RequestAddStatus.SUCCESS else RequestAddStatus.FAILURE
	}
	if (status == RequestAddStatus.SUCCESS) {
		sendToReceiver(userId, receiverId)
	}
	call.respondDTO(status)
}

/**
 * 发送给对方
 */
private suspend fun sendToReceiver(senderId: Int, receiverId: Int) {
	val session = getSession(receiverId) ?: return
	session.sendMessage(WebSocketType.FRIEND_RECEIVED_REQUEST, senderId)
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