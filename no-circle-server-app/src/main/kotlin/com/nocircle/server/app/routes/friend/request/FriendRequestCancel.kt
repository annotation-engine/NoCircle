package com.nocircle.server.app.routes.friend.request

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.POST
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRequestDao
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
 * 取消好友请求
 */
@Authentication
@POST("friend/request/cancel")
suspend fun RoutingContext.cancelFriendRequest(): ApiResult<Unit> {
	val userId = call.getPrincipal().userId
	val parameters = call.receiveParameters()
	val id: Int by parameters
	val targetId: Int by parameters
	
	val success = transaction {
		FriendRequestDao.updateOne(id, userId, targetId, FriendRequestDTO.Status.CANCELED)
	}
	if (success) {
		sendToReceiver(WebSocketType.FRIEND_RECEIVED_REQUEST, userId, targetId)
		sendToReceiver(WebSocketType.FRIEND_RECEIVED_REQUEST_COUNT, userId, targetId)
	}
	val code = if (success) NoCode.FRIEND_REQUEST_CANCEL_SUCCESS else NoCode.FRIEND_REQUEST_CANCEL_FAILURE
	return ApiResult.new(code)
}