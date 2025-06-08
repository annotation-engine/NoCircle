package com.nocircle.server.app.services.auth

import com.nocircle.server.app.tables.user.UserLogins
import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.services.NoParameters
import com.nocircle.server.common.services.NoService
import io.ktor.http.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 授权验证
 */
object AuthVerifyTokenService : NoService<Boolean> {
	
	override val path = "/auth/verifyToken"
	
	override val method = HttpMethod.Post
	
	override val auth = true
	
	override suspend fun process(parameters: NoParameters): ApiResult<Boolean> {
		val userId = parameters.userId
		transaction {
			UserLogins.insert(userId, UserLogins.Method.Token)
		}
		return ApiResult.success("验证成功")
	}
}