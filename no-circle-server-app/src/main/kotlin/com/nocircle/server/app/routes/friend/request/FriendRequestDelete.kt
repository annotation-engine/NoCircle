package com.nocircle.server.app.routes.friend.request

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.POST
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.common.expends.create
import com.nocircle.server.common.expends.getPrincipal
import com.nocircle.shared.model.ApiResult
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 删除好友请求
 */
@Authentication
@POST("friend/request/delete")
suspend fun RoutingContext.deleteFriendRequest(): ApiResult<Unit> {
	val userId = call.getPrincipal().userId
	val parameters = call.receiveParameters()
	val targetId: Int by parameters
	
	val success = transaction {
		FriendRequestDao.deleteOne(userId, targetId)
	}
	val code = if (success) NoCode.FRIEND_REQUEST_DELETE_SUCCESS else NoCode.FRIEND_REQUEST_DELETE_FAILURE
	return ApiResult.create(code)
}