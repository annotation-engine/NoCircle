package com.nocircle.server.app.routes.friend.request

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.GET
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.common.expends.create
import com.nocircle.server.common.expends.getPrincipal
import com.nocircle.shared.model.ApiResult
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 查询未处理的消息数
 */
@Authentication
@GET("friend/request/queryPendingCount")
fun RoutingContext.queryFriendRequestPendingCount(): ApiResult<Int> {
	val userId = call.getPrincipal().userId
	val count = transaction {
		FriendRequestDao.getReceivedPendingRequestCount(userId)
	}
	return ApiResult.create(count, NoCode.FRIEND_REQUEST_QUERY_PENDING_COUNT_SUCCESS)
}