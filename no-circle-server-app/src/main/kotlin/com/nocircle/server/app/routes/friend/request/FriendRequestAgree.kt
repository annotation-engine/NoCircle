package com.nocircle.server.app.routes.friend.request

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.Field
import cn.ktorfitx.server.annotation.POST
import cn.ktorfitx.server.annotation.Principal
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRelationshipDao
import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.common.expends.create
import com.nocircle.server.common.exposed.tx
import com.nocircle.server.common.model.NoPrincipal
import com.nocircle.server.common.websockets.sendToReceiver
import com.nocircle.shared.model.ApiResult
import com.nocircle.shared.model.friend.request.FriendRequestDTO
import com.nocircle.shared.websocket.WebSocketType

/**
 * 同意好友请求
 */
@Authentication
@POST("friend/request/agree")
suspend fun agreeFriendRequest(
	@Principal principal: NoPrincipal,
	@Field id: Int,
	@Field targetId: Int
): ApiResult<Unit> {
	val userId = principal.userId
	val status = tx {
		val success = FriendRequestDao.updateOne(id, targetId, userId, FriendRequestDTO.Status.AGREED)
		if (!success) return@tx null
		FriendRelationshipDao.insertOne(userId, targetId)
		FriendRelationshipDao.insertOne(targetId, userId)
		FriendRequestDao.deleteOne(userId, targetId)
	}
	if (status == null) {
		return ApiResult.create(NoCode.FRIEND_REQUEST_AGREE_FAILURE)
	}
	sendToReceiver(WebSocketType.FRIEND_SENT_REQUEST, userId, targetId)
	sendToReceiver(WebSocketType.FRIEND_RECEIVED_REQUEST_COUNT, userId, userId)
	sendToReceiver(WebSocketType.FRIEND_LIST, userId, targetId)
	if (status) {
		sendToReceiver(WebSocketType.FRIEND_SENT_REQUEST, userId, userId)
	}
	return ApiResult.create(NoCode.FRIEND_REQUEST_AGREE_SUCCESS)
}