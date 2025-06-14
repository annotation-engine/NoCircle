package com.nocircle.server.app.routes.auth

import com.nocircle.server.app.dao.UserLoginDao
import com.nocircle.server.app.plugins.AuthContext
import com.nocircle.server.app.tables.UserLogins
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.noPrincipal
import com.nocircle.server.common.model.respondDTO
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 授权验证
 */
context(_: AuthContext)
fun Route.postVerifyToken(): Route = post("verifyToken") {
	val userId = call.noPrincipal!!.userId
	transaction {
		UserLoginDao.insertOne(userId, UserLogins.Method.TOKEN)
	}
	call.respondDTO(VerifyTokenStatus.SUCCESS)
}

/**
 * 0 - 999
 */
enum class VerifyTokenStatus(
	override val msg: String,
	override val code: Int
) : NoStatus {
	SUCCESS("验证成功", 0)
}