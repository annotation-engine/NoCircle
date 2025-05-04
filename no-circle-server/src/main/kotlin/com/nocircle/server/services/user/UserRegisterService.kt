package com.nocircle.server.services.user

import com.nocircle.server.annotations.Schedule
import com.nocircle.server.annotations.ServiceSchedule
import com.nocircle.server.models.ApiResult
import com.nocircle.server.models.Code
import com.nocircle.server.services.NoParameters
import com.nocircle.server.services.NoService
import com.nocircle.server.tables.UserTable
import com.nocircle.server.tables.isLogicExists
import com.nocircle.server.utils.PasswordUtils
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
	
	override suspend fun receiver(call: RoutingCall): NoParameters? {
		val parameters = call.receiveParameters()
		return NoParameters.create(
			"username" to parameters.getOrFail("username"),
			"password" to parameters.getOrFail("password")
		)
	}
	
	override suspend fun service(parameters: NoParameters): ApiResult<Unit> {
		val username: String = parameters["username"]
		val password: String = parameters["password"]
		val success = newSuspendedTransaction {
			val empty = UserTable.selectAll()
				.where { UserTable.username eq username and UserTable.isLogicExists }
				.empty()
			if (!empty) return@newSuspendedTransaction false
			val insert = UserTable.insert {
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