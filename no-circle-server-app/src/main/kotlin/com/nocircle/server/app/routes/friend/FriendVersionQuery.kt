package com.nocircle.server.app.routes.friend

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.GET
import cn.ktorfitx.server.annotation.Principal
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendVersionDao
import com.nocircle.server.common.expends.create
import com.nocircle.server.common.model.NoPrincipal
import com.nocircle.shared.model.ApiResult
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

@Authentication
@GET("friend/version/query")
fun queryFriendVersion(
	@Principal principal: NoPrincipal
): ApiResult<Int> {
	val userId = principal.userId
	val version = transaction {
		FriendVersionDao.getVersionByUserId(userId) ?: 0
	}
	return ApiResult.create(version, NoCode.FRIEND_VERSION_QUERY_SUCCESS)
}