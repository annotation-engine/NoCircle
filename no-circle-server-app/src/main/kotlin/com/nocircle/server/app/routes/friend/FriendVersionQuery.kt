package com.nocircle.server.app.routes.friend

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.GET
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendVersionDao
import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.expends.getPrincipal
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

@Authentication
@GET("friend/version/query")
fun RoutingContext.queryFriendVersion(): ApiResult<Int> {
	val userId = call.getPrincipal().userId
	val version = transaction {
		FriendVersionDao.getVersionByUserId(userId) ?: 0
	}
	return ApiResult.new(version, NoCode.FRIEND_VERSION_QUERY_SUCCESS)
}