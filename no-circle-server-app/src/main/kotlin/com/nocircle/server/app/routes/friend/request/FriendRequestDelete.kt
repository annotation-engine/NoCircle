package com.nocircle.server.app.routes.friend.request

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.Field
import cn.ktorfitx.server.annotation.POST
import cn.ktorfitx.server.annotation.Principal
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.common.expends.create
import com.nocircle.server.common.exposed.tx
import com.nocircle.server.common.model.NoPrincipal
import com.nocircle.shared.model.ApiResult

/**
 * 删除好友请求
 */
@Authentication
@POST("friend/request/delete")
suspend fun deleteFriendRequest(
	@Principal principal: NoPrincipal,
	@Field targetId: Int
): ApiResult<Unit> {
	val userId = principal.userId
	val success = tx {
		FriendRequestDao.deleteOne(userId, targetId)
	}
	val code = if (success) NoCode.FRIEND_REQUEST_DELETE_SUCCESS else NoCode.FRIEND_REQUEST_DELETE_FAILURE
	return ApiResult.create(code)
}