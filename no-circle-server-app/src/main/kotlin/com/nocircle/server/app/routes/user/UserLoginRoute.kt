package com.nocircle.server.app.routes.user

import com.nocircle.server.app.plugins.UserToken
import com.nocircle.server.app.plugins.redisson
import com.nocircle.server.app.plugins.yaml
import com.nocircle.server.app.tables.user.UserLogins
import com.nocircle.server.app.tables.user.Users
import com.nocircle.server.app.utils.JWTUtils
import com.nocircle.server.app.utils.PasswordUtils
import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.route.NoParameters
import com.nocircle.server.common.route.NoRoute
import com.nocircle.server.common.route.noParameters
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.time.toJavaDuration

/**
 * 用户登录
 */
object UserLoginRoute : NoRoute<UserLoginRoute.UserLogin> {
	
	override val path = "/user/login"
	
	override val method = HttpMethod.Post
	
	override suspend fun receive(call: RoutingCall) = noParameters {
		val parameters = call.receiveParameters()
		this["username"] = parameters.getString("username")
		this["password"] = parameters.getString("password")
	}
	
	override suspend fun process(parameters: NoParameters): ApiResult<UserLogin> {
		val username: String by parameters
		val password: String by parameters
		val user = transaction {
			Users.getByUsername(username)
		}
		if (user == null || !PasswordUtils.verity(password, user.password)) {
			return ApiResult.failure("用户名或密码错误")
		}
		
		transaction {
			UserLogins.insert(user.id.value, UserLogins.Method.Password)
		}
		
		val token = JWTUtils.generate(user.id.value, user.username)
		val bucket = redisson.getBucket<String>("${UserToken.prefix}${user.id}")
		bucket.set(token, yaml.jwt.timeout.toJavaDuration())
		val data = UserLogin(token)
		return ApiResult.success(data, "登录成功")
	}
	
	@Serializable
	data class UserLogin(
		val token: String,
	)
}