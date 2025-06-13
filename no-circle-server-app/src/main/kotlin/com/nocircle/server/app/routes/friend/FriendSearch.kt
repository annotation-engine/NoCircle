package com.nocircle.server.app.routes.friend

import com.nocircle.server.app.plugins.FriendContext
import com.nocircle.server.app.tables.friend.FriendAddRequests
import com.nocircle.server.app.tables.friend.FriendRelationships
import com.nocircle.server.app.tables.user.UserLabels
import com.nocircle.server.app.tables.user.Users
import com.nocircle.server.common.exposed.getString
import com.nocircle.server.common.model.Status
import com.nocircle.server.common.model.noPrincipal
import com.nocircle.server.common.model.respond
import com.nocircle.server.common.routes.AuthenticateContext
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

context(_: FriendContext, _: AuthenticateContext)
fun Route.getSearch() = get("search") {
	val userId = call.noPrincipal!!.userId
	val queryUsername = call.queryParameters.getString("username")
	if (queryUsername.isBlank()) {
		return@get call.respond(SearchStatus.USERNAME_NOT_EMPTY)
	}
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
	} ?: return@get call.respond(SearchStatus.NOT_FOUND)
	
	call.respond(searchUser, SearchStatus.SUCCESS)
}

@Serializable
private data class SearchUser(
	val userId: Int,
	val username: String,
	val nickname: String?,
	val avatarUrl: String?,
	val labels: List<Label>,
	val relationship: Relationship,
	val isAlreadySend: Boolean
)

@Serializable
private data class Label(
	val id: Int,
	val label: String,
	val color: Int,
)

private enum class Relationship {
	FRIEND,
	OWNER,
	STRANGER
}

/**
 * 300X
 */
private enum class SearchStatus(
	override val msg: String,
	override val code: Int
) : Status {
	SUCCESS("搜索成功", 0),
	USERNAME_NOT_EMPTY("用户名不能为空", 3000),
	NOT_FOUND("未搜索到该用户", 3001)
}