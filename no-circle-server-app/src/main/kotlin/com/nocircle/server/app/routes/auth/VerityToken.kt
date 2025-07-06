package com.nocircle.server.app.routes.auth

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.POST
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.UserLoginDao
import com.nocircle.server.app.tables.UserLogins
import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.expends.getPrincipal
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 授权验证
 */
@Authentication
@POST("auth/verityToken")
fun RoutingContext.verifyToken(): ApiResult<Unit> {
	val userId = call.getPrincipal().userId
	transaction {
		UserLoginDao.insertOne(userId, UserLogins.Method.TOKEN)
	}
	return ApiResult.new(NoCode.AUTH_VERIFY_TOKEN_SUCCESS)
}