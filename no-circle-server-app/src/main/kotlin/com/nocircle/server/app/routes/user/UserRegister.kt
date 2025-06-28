package com.nocircle.server.app.routes.user

import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.plugins.UserRouteContext
import com.nocircle.server.common.expends.isLowerCases
import com.nocircle.server.common.model.respondOK
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 用户注册
 */
context(_: UserRouteContext)
fun Route.userRegister() = post("register") {
	val parameters = call.receiveParameters()
	val username: String by parameters
	val password: String by parameters
	val nickname: String by parameters
	if (username.length !in 8..12 || !username.isLowerCases() || password.length !in 8..20 || nickname.isBlank() || nickname.length > 20) {
		return@post call.respondOK(NoCode.USER_REGISTER_FAILURE)
	}
	val status = transaction {
		val exists = UserDao.isExistsByUsername(username)
		if (exists) {
			return@transaction NoCode.USER_REGISTER_USER_ALREADY_EXISTS
		}
		val success = UserDao.insertOne(username, password, nickname)
		if (success) NoCode.USER_REGISTER_SUCCESS else NoCode.USER_REGISTER_FAILURE
	}
	call.respondOK(status)
}