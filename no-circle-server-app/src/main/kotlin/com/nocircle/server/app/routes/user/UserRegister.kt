package com.nocircle.server.app.routes.user

import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.plugins.UserRouteGroup
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.respondOK
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 用户注册
 */
context(_: UserRouteGroup)
fun Route.postUserRegister() = post("register") {
	val parameters = call.receiveParameters()
	val username: String by parameters
	val password: String by parameters
	
	val status = transaction {
		val exists = UserDao.isExistsByUsername(username)
		if (exists) {
			return@transaction RegisterStatus.USER_ALREADY_EXISTS
		}
		val success = UserDao.insertOne(username, password)
		if (success) RegisterStatus.SUCCESS else RegisterStatus.FAILURE
	}
	call.respondOK(status)
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