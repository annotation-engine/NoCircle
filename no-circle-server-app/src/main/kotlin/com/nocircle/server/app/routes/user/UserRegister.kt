package com.nocircle.server.app.routes.user

import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.plugins.UserContext
import com.nocircle.server.common.exposed.getString
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.respondDTO
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 用户注册
 */
context(_: UserContext)
fun Route.postUserRegister() = post("register") {
	val parameters = call.receiveParameters()
	val username = parameters.getString("username")
	val password = parameters.getString("password")
	
	val status = transaction {
		val exists = UserDao.isExistsByUsername(username)
		if (exists) {
			return@transaction RegisterStatus.USER_ALREADY_EXISTS
		}
		val success = UserDao.insertOne(username, password)
		if (success) RegisterStatus.SUCCESS else RegisterStatus.FAILURE
	}
	call.respondDTO(status)
}

/**
 * 103X
 */
private enum class RegisterStatus(
	override val msg: String,
	override val code: Int
) : NoStatus {
	SUCCESS("用户注册成功，请前往登录", 0),
	USER_ALREADY_EXISTS("用户已经存在，请前往登录", 1030),
	FAILURE("注册失败", 1031)
}