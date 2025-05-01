package com.nocircle.service.services.user

import com.nocircle.service.model.ApiResult
import com.nocircle.service.model.Code
import com.nocircle.service.services.KtorService
import com.nocircle.service.tables.UserTable
import com.nocircle.service.tables.isLogicExists
import com.nocircle.service.utils.PasswordUtils
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object UserRegisterService : KtorService<Unit> {
	
	override val path = "/user/register"
	
	override val method = HttpMethod.Post
	
	context(call: RoutingCall)
	override suspend fun service(): ApiResult<Unit> {
		val parameters = call.receiveParameters()
		val username = parameters.getOrFail("username")
		val password = parameters.getOrFail("password")
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