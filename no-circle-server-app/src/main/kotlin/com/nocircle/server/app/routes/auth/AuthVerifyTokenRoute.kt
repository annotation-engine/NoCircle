package com.nocircle.server.app.routes.auth

import com.nocircle.server.app.plugins.AuthContext
import com.nocircle.server.app.tables.user.UserLogins
import com.nocircle.server.common.model.Status
import com.nocircle.server.common.model.noPrincipal
import com.nocircle.server.common.model.respond
import com.nocircle.server.common.routes.AuthenticateContext
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 授权验证
 */
context(_: AuthContext, _: AuthenticateContext)
fun Route.postVerifyToken(): Route = post("verifyToken") {
	val userId = call.noPrincipal!!.userId
	transaction {
		UserLogins.insertOne(userId, UserLogins.Method.TOKEN)
	}
	call.respond(VerifyTokenStatus.SUCCESS)
}

/**
 * 0 - 999
 */
enum class VerifyTokenStatus(
	override val msg: String,
	override val code: Int
) : Status {
	SUCCESS("验证成功", 0)
}