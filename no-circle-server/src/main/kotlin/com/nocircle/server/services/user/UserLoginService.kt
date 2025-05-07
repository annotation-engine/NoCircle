package com.nocircle.server.services.user

import com.nocircle.server.annotations.Schedule
import com.nocircle.server.annotations.ServiceSchedule
import com.nocircle.server.models.ApiResult
import com.nocircle.server.models.Code
import com.nocircle.server.plugins.RedisPrefix
import com.nocircle.server.plugins.redisson
import com.nocircle.server.plugins.yaml
import com.nocircle.server.services.NoParameters
import com.nocircle.server.services.NoService
import com.nocircle.server.services.noParameters
import com.nocircle.server.tables.isLogicExists
import com.nocircle.server.tables.user.User
import com.nocircle.server.tables.user.Users
import com.nocircle.server.utils.JWTUtils
import com.nocircle.server.utils.PasswordUtils
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlin.time.toJavaDuration

/**
 * 用户登录服务
 */
@ServiceSchedule(schedule = Schedule.Release)
object UserLoginService : NoService<UserLoginService.UserLogin> {
	
	override val path = "/user/login"
	
	override val method = HttpMethod.Post
	
	override suspend fun receive(call: RoutingCall) = noParameters {
		val parameters = call.receiveParameters()
		this["username"] = parameters.getOrFail("username")
		this["password"] = parameters.getOrFail("password")
	}
	
	override suspend fun process(parameters: NoParameters): ApiResult<UserLogin> {
		val username: String by parameters
		val password: String by parameters
		val user = newSuspendedTransaction {
			val row = Users.select(Users.id, Users.username, Users.password)
				.where { Users.username eq username and Users.isLogicExists }
				.singleOrNull() ?: return@newSuspendedTransaction null
			User.wrapRow(row)
		}
		if (user == null || !PasswordUtils.verity(password, user.password)) {
			return ApiResult.failure("用户名或密码错误", Code.User.Login.USERNAME_OR_PASSWORD_ERROR)
		}
		val token = JWTUtils.generate(user.id.value, user.username)
		val bucket = redisson.getBucket<String>("${RedisPrefix.USER_TOKEN}${user.id}")
		bucket.set(token, yaml.jwt.timeout.toJavaDuration())
		val data = UserLogin(token)
		return ApiResult.success(data, "登录成功")
	}
	
	@Serializable
	data class UserLogin(
		val token: String,
	)
}