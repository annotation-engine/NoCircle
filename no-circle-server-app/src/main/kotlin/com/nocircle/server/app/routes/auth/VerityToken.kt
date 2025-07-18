package com.nocircle.server.app.routes.auth

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.POST
import cn.ktorfitx.server.annotation.Principal
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.UserLoginDao
import com.nocircle.server.app.tables.UserLogins
import com.nocircle.server.common.expends.create
import com.nocircle.server.common.model.NoPrincipal
import com.nocircle.shared.model.ApiResult
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 授权验证
 */
@Authentication
@POST("auth/verifyToken")
fun verifyToken(
	@Principal principal: NoPrincipal
): ApiResult<Boolean> {
	val userId = principal.userId
	val success = transaction {
		UserLoginDao.insertOne(userId, UserLogins.Method.TOKEN)
	}
	return ApiResult.create(success, NoCode.AUTH_VERIFY_TOKEN_SUCCESS)
}