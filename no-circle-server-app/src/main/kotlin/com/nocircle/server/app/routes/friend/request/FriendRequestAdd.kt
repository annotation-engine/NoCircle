package com.nocircle.server.app.routes.friend.request

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.POST
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.expends.getPrincipal
import com.nocircle.server.common.websockets.sendToReceiver
import com.nocircle.shared.model.friend.request.FriendRequestDTO
import com.nocircle.shared.websocket.WebSocketType
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 添加好友请求
 */
@Authentication
@POST("friend/request/add")
suspend fun RoutingContext.addFriendRequest(): ApiResult<Unit> {
	val userId = call.getPrincipal().userId
	val parameters = call.receiveParameters()
	val targetId: Int by parameters
	if (userId == targetId) {
		return ApiResult.new(NoCode.FRIEND_REQUEST_ADD_CANNOT_ADD_ONESELF)
	}
	val code = transaction {
		val isExists = UserDao.isExistsByUserId(targetId)
		if (!isExists) return@transaction NoCode.FRIEND_REQUEST_ADD_USER_NOT_FOUND
		val request = FriendRequestDao.getOneBySenderIdAndReceiverId(userId, targetId)
		if (request != null) {
			if (request.status == FriendRequestDTO.Status.PENDING) {
				return@transaction NoCode.FRIEND_REQUEST_ADD_REPEATED
			} else {
				FriendRequestDao.deleteOne(userId, targetId)
			}
		}
		val success = FriendRequestDao.insertOne(userId, targetId)
		if (success) NoCode.FRIEND_REQUEST_ADD_SUCCESS else NoCode.FRIEND_REQUEST_ADD_FAILURE
	}
	if (code == NoCode.FRIEND_REQUEST_ADD_SUCCESS) {
		sendToReceiver(WebSocketType.FRIEND_RECEIVED_REQUEST, userId, targetId)
		sendToReceiver(WebSocketType.FRIEND_RECEIVED_REQUEST_COUNT, userId, targetId)
	}
	return ApiResult.new(code)
}