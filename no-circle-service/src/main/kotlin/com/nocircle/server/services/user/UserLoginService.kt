package com.nocircle.server.services.user

import com.nocircle.server.model.ApiResult
import com.nocircle.server.model.Code
import com.nocircle.server.services.KtorService
import com.nocircle.server.tables.User
import com.nocircle.server.tables.UserTable
import com.nocircle.server.tables.isLogicExists
import com.nocircle.server.utils.JWTUtils
import com.nocircle.server.utils.PasswordUtils
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object UserLoginService : KtorService<UserLoginService.UserLogin> {
	
	override val path = "/user/login"
	
	override val method = HttpMethod.Post
	
	context(call: RoutingCall)
	override suspend fun service(): ApiResult<UserLogin> {
		val parameters = call.receiveParameters()
		val username = parameters.getOrFail("username")
		val password = parameters.getOrFail("password")
		val user = newSuspendedTransaction {
			val row = UserTable.select(UserTable.id, UserTable.username, UserTable.password)
				.where { UserTable.username eq username and UserTable.isLogicExists }
				.singleOrNull() ?: return@newSuspendedTransaction null
			User.wrapRow(row)
		}
		if (user == null) {
			return ApiResult.failure("用户名或密码错误", Code.User.Login.USERNAME_OR_PASSWORD_ERROR)
		}
		val verify = PasswordUtils.verity(password, user.password)
		if (!verify) {
			return ApiResult.failure("用户名或密码错误", Code.User.Login.USERNAME_OR_PASSWORD_ERROR)
		}
		val token = JWTUtils.generate(user.id.value, user.username)
		val data = UserLogin(token)
		return ApiResult.success(data, "登录成功")
	}
	
	@Serializable
	data class UserLogin(
		val token: String,
	)
}