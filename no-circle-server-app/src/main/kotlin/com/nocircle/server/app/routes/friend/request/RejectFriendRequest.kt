package com.nocircle.server.app.routes.friend.request

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.Field
import cn.ktorfitx.server.annotation.POST
import cn.ktorfitx.server.annotation.Principal
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.common.expends.create
import com.nocircle.server.common.model.NoPrincipal
import com.nocircle.server.common.websockets.sendToReceiver
import com.nocircle.shared.model.ApiResult
import com.nocircle.shared.model.friend.request.FriendRequestDTO
import com.nocircle.shared.websocket.WebSocketType
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 拒绝好友请求
 */
@Authentication
@POST("friend/request/reject")
suspend fun rejectFriendRequest(
	@Principal principal: NoPrincipal,
	@Field id: Int,
	@Field targetId: Int
): ApiResult<Unit> {
	val userId = principal.userId
	val success = transaction {
		FriendRequestDao.updateOne(id, targetId, userId, FriendRequestDTO.Status.REJECTED)
	}
	if (success) {
		sendToReceiver(WebSocketType.FRIEND_SENT_REQUEST, userId, targetId)
		sendToReceiver(WebSocketType.FRIEND_RECEIVED_REQUEST_COUNT, userId, userId)
	}
	val code = if (success) NoCode.FRIEND_REQUEST_REJECT_SUCCESS else NoCode.FRIEND_REQUEST_REJECT_FAILURE
	return ApiResult.create(code)
}