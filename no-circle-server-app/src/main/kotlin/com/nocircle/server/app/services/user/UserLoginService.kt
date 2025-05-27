package com.nocircle.server.app.services.user

import com.nocircle.server.app.plugins.RedisPrefix
import com.nocircle.server.app.plugins.redisson
import com.nocircle.server.app.plugins.yaml
import com.nocircle.server.app.tables.user.User
import com.nocircle.server.app.tables.user.Users
import com.nocircle.server.app.utils.JWTUtils
import com.nocircle.server.app.utils.PasswordUtils
import com.nocircle.server.common.exposed.isLogicExists
import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.services.NoParameters
import com.nocircle.server.common.services.NoService
import com.nocircle.server.common.services.noParameters
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.time.toJavaDuration

/**
 * 用户登录服务
 */
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
		val user = transaction {
			val row = Users.select(Users.id, Users.username, Users.password)
				.where { Users.username eq username }
				.isLogicExists(Users)
				.singleOrNull() ?: return@transaction null
			User.wrapRow(row)
		}
		if (user == null || !PasswordUtils.verity(password, user.password)) {
			return ApiResult.failure("用户名或密码错误")
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