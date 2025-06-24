package com.nocircle.server.app.routes.auth

import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.UserLoginDao
import com.nocircle.server.app.plugins.AuthRouteContext
import com.nocircle.server.app.tables.UserLogins
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.AuthContext
import com.nocircle.server.common.routes.getPrincipal
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 授权验证
 */
context(_: AuthRouteContext, _: AuthContext)
fun Route.verifyToken(): Route = post("verifyToken") {
	val userId = call.getPrincipal().userId
	transaction {
		UserLoginDao.insertOne(userId, UserLogins.Method.TOKEN)
	}
	call.respondOK(NoCode.AUTH_VERIFY_TOKEN_SUCCESS)
}