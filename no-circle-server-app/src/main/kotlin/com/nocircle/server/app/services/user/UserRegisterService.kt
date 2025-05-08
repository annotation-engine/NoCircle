package com.nocircle.server.app.services.user

import com.nocircle.server.app.codes.Code
import com.nocircle.server.app.tables.user.Users
import com.nocircle.server.app.utils.PasswordUtils
import com.nocircle.server.common.annotations.Schedule
import com.nocircle.server.common.annotations.ServiceSchedule
import com.nocircle.server.common.models.ApiResult
import com.nocircle.server.common.services.NoParameters
import com.nocircle.server.common.services.NoService
import com.nocircle.server.common.services.noParameters
import com.nocircle.server.common.tables.isLogicExists
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

/**
 * 用户注册服务
 */
@ServiceSchedule(schedule = Schedule.Release)
object UserRegisterService : NoService<Unit> {
	
	override val path = "/user/register"
	
	override val method = HttpMethod.Post
	
	override suspend fun receive(call: RoutingCall) = noParameters {
		val parameters = call.receiveParameters()
		this["username"] = parameters.getOrFail("username")
		this["password"] = parameters.getOrFail("password")
	}
	
	override suspend fun process(parameters: NoParameters): ApiResult<Unit> {
		val username: String by parameters
		val password: String by parameters
		val success = newSuspendedTransaction {
			val empty = Users.selectAll()
				.where { Users.username eq username and Users.isLogicExists }
				.empty()
			if (!empty) return@newSuspendedTransaction false
			val insert = Users.insert {
				it[this.username] = username
				it[this.password] = PasswordUtils.encrypt(password)
			}
			insert.insertedCount == 1
		}
		return if (success) {
			ApiResult.success("用户注册成功，前往登录")
		} else {
			ApiResult.failure("用户已经存在，请前往登录", Code.User.Register.USERNAME_OR_PASSWORD_ERROR)
		}
	}
}