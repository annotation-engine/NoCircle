package com.nocircle.server.app.services.user

import com.nocircle.server.app.tables.user.UserLabels
import com.nocircle.server.app.tables.user.Users
import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.services.NoParameters
import com.nocircle.server.common.services.NoService
import com.nocircle.server.common.services.noParameters
import io.ktor.http.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 用户搜索
 */
object UserQueryService : NoService<UserQueryService.SearchUser> {
	
	override val path = "/user/query"
	
	override val method = HttpMethod.Get
	
	override val auth = true
	
	override suspend fun receive(call: RoutingCall) = noParameters(call) {
		this["queryUsername"] = call.queryParameters.getString("username")
	}
	
	override suspend fun process(parameters: NoParameters): ApiResult<SearchUser> {
		val queryUsername: String by parameters
		if (queryUsername.isBlank()) return ApiResult.failure("用户名不能为空")
		val searchUser = transaction {
			val user = Users.getByUsername(queryUsername) ?: return@transaction null
			val userId = user.id.value
			val labels = UserLabels.getListByUserId(userId).map {
				Label(it.id.value, it.label, it.color)
			}
			SearchUser(
				userId = user.id.value,
				username = user.username,
				nickname = user.nickname,
				avatarUrl = user.avatarUrl,
				labels = labels,
				isOwner = parameters.userId == userId
			)
		}
		return if (searchUser != null) {
			ApiResult.success(searchUser, "用户搜索成功")
		} else {
			ApiResult.failure("未搜索到用户")
		}
	}
	
	@Serializable
	data class SearchUser(
		val userId: Int,
		val username: String,
		val nickname: String?,
		val avatarUrl: String?,
		val labels: List<Label>,
		val isOwner: Boolean
	)
	
	@Serializable
	data class Label(
		val id: Int,
		val label: String,
		val color: Int,
	)
}