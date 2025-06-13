package com.nocircle.server.app.routes.user

import com.nocircle.server.app.tables.user.Users
import com.nocircle.server.common.exposed.getString
import com.nocircle.server.common.model.Status
import com.nocircle.server.common.model.respond
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 用户注册
 */
fun Route.postRegister() = post("register") {
	val parameters = call.receiveParameters()
	val username = parameters.getString("username")
	val password = parameters.getString("password")
	
	val status = transaction {
		val exists = Users.isExistsByUsername(username)
		if (exists) {
			return@transaction RegisterStatus.USER_ALREADY_EXISTS
		}
		val success = Users.insertOne(username, password)
		if (success) RegisterStatus.SUCCESS else RegisterStatus.FAILURE
	}
	call.respond(status)
}

/**
 * 103X
 */
private enum class RegisterStatus(
	override val msg: String,
	override val code: Int
) : Status {
	SUCCESS("用户注册成功，请前往登录", 0),
	USER_ALREADY_EXISTS("用户已经存在，请前往登录", 1030),
	FAILURE("注册失败", 1031)
}