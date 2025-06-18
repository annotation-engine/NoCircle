package com.nocircle.server.app.routes.user

import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.dao.UserLoginDao
import com.nocircle.server.app.plugins.UserRouteGroup
import com.nocircle.server.app.plugins.UserToken
import com.nocircle.server.app.plugins.redisson
import com.nocircle.server.app.plugins.yaml
import com.nocircle.server.app.tables.UserLogins
import com.nocircle.server.app.utils.JWTUtils
import com.nocircle.server.app.utils.PasswordUtils
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.app.code.NoCode
import com.nocircle.shared.model.user.UserLoginDTO
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.time.toJavaDuration

/**
 * 用户登录
 */
context(_: UserRouteGroup)
fun Route.postUserLogin() = post("login") {
	val parameters = call.receiveParameters()
	val username: String by parameters
	val password: String by parameters
	val user = transaction {
		UserDao.getOneByUsername(username)
	}
	if (user == null || !PasswordUtils.verity(password, user.password)) {
		return@post call.respondOK(NoCode.USER_LOGIN_USERNAME_OR_PASSWORD_ERROR)
	}
	transaction {
		UserLoginDao.insertOne(user.id.value, UserLogins.Method.PASSWORD)
	}
	val token = JWTUtils.generate(user.id.value, user.username)
	val bucket = redisson.getBucket<String>("${UserToken.prefix}${user.id}")
	bucket.set(token, yaml.jwt.timeout.toJavaDuration())
	val userLogin = UserLoginDTO(token)
	call.respondOK(userLogin, NoCode.USER_LOGIN_SUCCESS)
}