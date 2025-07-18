package com.nocircle.server.app.routes.friend.request

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.GET
import cn.ktorfitx.server.annotation.Principal
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.common.expends.create
import com.nocircle.server.common.model.NoPrincipal
import com.nocircle.shared.model.ApiResult
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 查询未处理的消息数
 */
@Authentication
@GET("friend/request/queryPendingCount")
fun queryFriendRequestPendingCount(
	@Principal principal: NoPrincipal,
): ApiResult<Int> {
	val userId = principal.userId
	val count = transaction {
		FriendRequestDao.getReceivedPendingRequestCount(userId)
	}
	return ApiResult.create(count, NoCode.FRIEND_REQUEST_QUERY_PENDING_COUNT_SUCCESS)
}