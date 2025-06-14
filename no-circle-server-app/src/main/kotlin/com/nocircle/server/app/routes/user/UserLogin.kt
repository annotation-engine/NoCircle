package com.nocircle.server.app.routes.user

import com.nocircle.server.app.plugins.UserContext
import com.nocircle.server.app.plugins.UserToken
import com.nocircle.server.app.plugins.redisson
import com.nocircle.server.app.plugins.yaml
import com.nocircle.server.app.tables.user.UserLogins
import com.nocircle.server.app.tables.user.Users
import com.nocircle.server.app.utils.JWTUtils
import com.nocircle.server.app.utils.PasswordUtils
import com.nocircle.server.common.exposed.getString
import com.nocircle.server.common.model.Status
import com.nocircle.server.common.model.respond
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.time.toJavaDuration

/**
 * 用户登录
 */
context(_: UserContext)
fun Route.postLogin() = post("login") {
	val parameters = call.receiveParameters()
	val username = parameters.getString("username")
	val password = parameters.getString("password")
	val user = transaction {
		Users.getOneByUsername(username)
	}
	if (user == null || !PasswordUtils.verity(password, user.password)) {
		return@post call.respond(LoginStatus.USERNAME_OR_PASSWORD_ERROR)
	}
	transaction {
		UserLogins.insertOne(user.id.value, UserLogins.Method.PASSWORD)
	}
	val token = JWTUtils.generate(user.id.value, user.username)
	val bucket = redisson.getBucket<String>("${UserToken.prefix}${user.id}")
	bucket.set(token, yaml.jwt.timeout.toJavaDuration())
	val data = UserLogin(token)
	call.respond(data, LoginStatus.SUCCESS)
}

@Serializable
private data class UserLogin(
	val token: String,
)

/**
 * 100X
 */
private enum class LoginStatus(
	override val msg: String,
	override val code: Int
) : Status {
	SUCCESS("登录成功", 0),
	USERNAME_OR_PASSWORD_ERROR("用户名或密码错误", 1000)
}