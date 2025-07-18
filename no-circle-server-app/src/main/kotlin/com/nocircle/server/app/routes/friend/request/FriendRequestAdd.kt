package com.nocircle.server.app.routes.friend.request

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.Field
import cn.ktorfitx.server.annotation.POST
import cn.ktorfitx.server.annotation.Principal
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.common.expends.create
import com.nocircle.server.common.model.NoPrincipal
import com.nocircle.server.common.websockets.sendToReceiver
import com.nocircle.shared.model.ApiResult
import com.nocircle.shared.model.friend.request.FriendRequestDTO
import com.nocircle.shared.websocket.WebSocketType
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 添加好友请求
 */
@Authentication
@POST("friend/request/add")
suspend fun addFriendRequest(
	@Principal principal: NoPrincipal,
	@Field targetId: Int
): ApiResult<Unit> {
	val userId = principal.userId
	if (userId == targetId) {
		return ApiResult.create(NoCode.FRIEND_REQUEST_ADD_CANNOT_ADD_ONESELF)
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
	return ApiResult.create(code)
}