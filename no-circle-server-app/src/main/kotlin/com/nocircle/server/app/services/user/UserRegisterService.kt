package com.nocircle.server.app.services.user

import com.nocircle.server.app.tables.user.Users
import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.services.NoParameters
import com.nocircle.server.common.services.NoService
import com.nocircle.server.common.services.noParameters
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 用户注册服务
 */
object UserRegisterService : NoService<Unit> {
	
	override val path = "/user/register"
	
	override val method = HttpMethod.Post
	
	override suspend fun receive(call: RoutingCall) = noParameters {
		val parameters = call.receiveParameters()
		this["username"] = parameters.getString("username")
		this["password"] = parameters.getString("password")
	}
	
	override suspend fun process(parameters: NoParameters): ApiResult<Unit> {
		val username: String by parameters
		val password: String by parameters
		val success = transaction {
			val exists = Users.isExistsByUsername(username)
			if (exists) return@transaction false
			Users.insert(username, password)
		}
		return if (success) {
			ApiResult.success("用户注册成功，前往登录")
		} else {
			ApiResult.failure("用户已经存在，请前往登录")
		}
	}
}