package com.nocircle.server.services.user

import com.nocircle.server.models.ApiResult
import com.nocircle.server.plugins.userPrincipal
import com.nocircle.server.services.NoParameters
import com.nocircle.server.services.NoService
import com.nocircle.server.services.noParameters
import com.nocircle.server.tables.User
import com.nocircle.server.tables.UserTable
import io.ktor.http.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object UserDetailService : NoService<UserDetailService.UserDetail> {
	
	override val path = "/user/detail"
	
	override val method = HttpMethod.Get
	
	override val auth = true
	
	override suspend fun receive(call: RoutingCall) = noParameters {
		val userId = call.userPrincipal.userId
		this["userId"] = userId
	}
	
	override suspend fun process(parameters: NoParameters): ApiResult<UserDetail> {
		val userId: Int by parameters
		val user = transaction {
			val row = UserTable.selectAll()
				.where { UserTable.id eq userId }
				.firstOrNull() ?: return@transaction null
			User.wrapRow(row)
		} ?: return ApiResult.failure("用户详情查询成功")
		val data = UserDetail(
			username = user.username,
			nickname = user.nickname,
			avatarUrl = user.avatarUrl
		)
		return ApiResult.success(data, "用户详情查询成功")
	}
	
	@Serializable
	data class UserDetail(
		val username: String,
		val nickname: String?,
		val avatarUrl: String?
	)
}