package com.nocircle.server.app.routes.friend

import com.nocircle.server.app.plugins.FriendContext
import com.nocircle.server.app.plugins.WebSocketType
import com.nocircle.server.app.tables.friend.FriendAddRequests
import com.nocircle.server.app.tables.user.Users
import com.nocircle.server.common.exposed.getInt
import com.nocircle.server.common.model.Status
import com.nocircle.server.common.model.noPrincipal
import com.nocircle.server.common.model.respond
import com.nocircle.server.common.websockets.getSession
import com.nocircle.server.common.websockets.sendMessage
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 添加好友请求
 */
context(_: FriendContext)
fun Route.postAddRequest() = post("addRequest") {
	val userId = call.noPrincipal!!.userId
	val receiverId = call.receiveParameters().getInt("receiverId")
	if (userId == receiverId) {
		return@post call.respond(AddRequestStatus.CANNOT_ADD_ONESELF)
	}
	val status = transaction {
		val isExists = Users.isExistsByUserId(receiverId)
		if (!isExists) return@transaction AddRequestStatus.USER_NOT_FOUND
		val isAlready = FriendAddRequests.isExistsBySenderIdAndReceiverId(userId, receiverId)
		if (isAlready) return@transaction AddRequestStatus.REPEATED
		val success = FriendAddRequests.insertOne(userId, receiverId)
		if (success) AddRequestStatus.SUCCESS else AddRequestStatus.FAILURE
	}
	if (status == AddRequestStatus.SUCCESS) {
		sendToReceiver(userId, receiverId)
	}
	call.respond(status)
}

private suspend fun sendToReceiver(senderId: Int, receiverId: Int) {
	val session = getSession(receiverId) ?: return
	session.sendMessage(WebSocketType.FRIEND_ADD_REQUEST, senderId)
}

/**
 * 121x
 */
private enum class AddRequestStatus(
	override val msg: String,
	override val code: Int
) : Status {
	SUCCESS("添加请求已发送", 0),
	CANNOT_ADD_ONESELF("不能添加自己为好友", 1210),
	USER_NOT_FOUND("对方用户不存在", 1211),
	REPEATED("请勿重复发送", 1212),
	FAILURE("添加请求发送失败", 1213)
}