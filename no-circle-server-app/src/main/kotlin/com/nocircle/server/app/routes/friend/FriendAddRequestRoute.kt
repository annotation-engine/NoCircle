package com.nocircle.server.app.routes.friend

import com.nocircle.server.app.plugins.WebSocketType
import com.nocircle.server.app.tables.friend.FriendAddRequests
import com.nocircle.server.app.tables.user.Users
import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.route.NoParameters
import com.nocircle.server.common.route.NoRoute
import com.nocircle.server.common.route.noParameters
import com.nocircle.server.common.websockets.getSession
import com.nocircle.server.common.websockets.sendMessage
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 好友添加请求
 */
object FriendAddRequestRoute : NoRoute<Unit> {
	
	override val path = "/friend/addRequest"
	
	override val method = HttpMethod.Post
	
	override val auth = true
	
	override suspend fun receive(call: RoutingCall) = noParameters(call) {
		this["receiverId"] = call.receiveParameters().getInt("receiverId")
	}
	
	override suspend fun process(parameters: NoParameters): ApiResult<Unit> {
		val userId = parameters.userId
		val receiverId: Int by parameters
		if (userId == receiverId) {
			return ApiResult.failure(Status.CANNOT_ADD_ONESELF.msg)
		}
		val status = transaction {
			val isExists = Users.isExistsByUserId(receiverId)
			if (!isExists) return@transaction Status.USER_NOT_FOUND
			val isAlready = FriendAddRequests.isExistsBySenderIdAndReceiverId(userId, receiverId)
			if (isAlready) return@transaction Status.REPEATED
			val success = FriendAddRequests.insertOne(userId, receiverId)
			if (success) Status.SUCCESS else Status.FAILURE
		}
		if (status == Status.SUCCESS) {
			sendToReceiver(userId, receiverId)
			return ApiResult.success(status.msg)
		} else {
			return ApiResult.failure(status.msg)
		}
	}
	
	private suspend fun sendToReceiver(senderId: Int, receiverId: Int) {
		val session = getSession(receiverId) ?: return
		session.sendMessage(WebSocketType.FRIEND_ADD_REQUEST, senderId)
	}
}

private enum class Status(
	val msg: String
) {
	SUCCESS("添加请求已发送"),
	CANNOT_ADD_ONESELF("不能添加自己为好友"),
	USER_NOT_FOUND("对方用户不存在"),
	REPEATED("请勿重复发送"),
	FAILURE("添加请求发送失败")
}