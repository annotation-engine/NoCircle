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
	
	override val method = HttpMethod.Companion.Get
	
	override suspend fun receive(call: RoutingCall) = noParameters {
		this["friendUsername"] = call.queryParameters.getString("username")
	}
	
	override suspend fun process(parameters: NoParameters): ApiResult<SearchUser> {
		val username: String by parameters
		val friendUsername: String by parameters
		if (username == friendUsername) {
			return ApiResult.failure("不能搜索自己")
		}
		val searchUser = transaction {
			val user = Users.getByUsername(friendUsername) ?: return@transaction null
			val userId = user.id.value
			val labels = UserLabels.getListByUserId(userId).map {
				Label(it.id.value, it.label, it.color)
			}
			SearchUser(
				userId = userId,
				username = user.username,
				nickname = user.nickname,
				avatarUrl = user.avatarUrl,
				labels = labels,
			)
		}
		return if (searchUser != null) {
			ApiResult.success(searchUser, "用户搜索成功")
		} else {
			ApiResult.success("未搜索到用户")
		}
	}
	
	@Serializable
	data class SearchUser(
		val userId: Int,
		val username: String,
		val nickname: String?,
		val avatarUrl: String?,
		val labels: List<Label>
	)
	
	@Serializable
	data class Label(
		val id: Int,
		val label: String,
		val color: Int,
	)
}