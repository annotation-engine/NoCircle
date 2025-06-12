package com.nocircle.server.app.routes.user

import com.nocircle.server.app.tables.friend.FriendAddRequests
import com.nocircle.server.app.tables.friend.FriendRelationships
import com.nocircle.server.app.tables.user.UserLabels
import com.nocircle.server.app.tables.user.Users
import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.route.NoParameters
import com.nocircle.server.common.route.NoRoute
import com.nocircle.server.common.route.noParameters
import io.ktor.http.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 用户搜索
 */
object UserQueryRoute : NoRoute<UserQueryRoute.SearchUser> {
	
	override val path = "/user/query"
	
	override val method = HttpMethod.Get
	
	override val auth = true
	
	override suspend fun receive(call: RoutingCall) = noParameters(call) {
		this["queryUsername"] = call.queryParameters.getString("username")
	}
	
	override suspend fun process(parameters: NoParameters): ApiResult<SearchUser> {
		val userId = parameters.userId
		val queryUsername: String by parameters
		if (queryUsername.isBlank()) return ApiResult.failure("用户名不能为空")
		val searchUser = transaction {
			val user = Users.getOneByUsername(queryUsername) ?: return@transaction null
			val targetId = user.id.value
			val labels = UserLabels.getListByUserId(targetId).map {
				Label(it.id.value, it.label, it.color)
			}
			val pair = if (userId == targetId) {
				Relationship.OWNER to false
			} else {
				val isFriend = FriendRelationships.isFriend(userId, targetId)
				val relationship = if (isFriend) Relationship.FRIEND else Relationship.STRANGER
				val isAlreadySend = FriendAddRequests.isExistsBySenderIdAndReceiverId(userId, targetId)
				relationship to isAlreadySend
			}
			SearchUser(
				userId = user.id.value,
				username = user.username,
				nickname = user.nickname,
				avatarUrl = user.avatarUrl,
				labels = labels,
				relationship = pair.first,
				isAlreadySend = pair.second
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
		val relationship: Relationship,
		val isAlreadySend: Boolean
	)
	
	@Serializable
	data class Label(
		val id: Int,
		val label: String,
		val color: Int,
	)
	
	enum class Relationship {
		FRIEND,
		OWNER,
		STRANGER
	}
}