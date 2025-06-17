package com.nocircle.server.app.routes.friend

import com.nocircle.server.app.dao.FriendRelationshipDao
import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.app.plugins.FriendRouteGroup
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.Authorized
import com.nocircle.server.common.routes.getPrincipal
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 搜索好友
 */
context(_: FriendRouteGroup, _: Authorized)
fun Route.getSearch() = get("search") {
	val userId = call.getPrincipal().userId
	val username: String by call.queryParameters
	if (username.isBlank()) {
		return@get call.respondOK(SearchStatus.USERNAME_NOT_EMPTY)
	}
	val searchUser = transaction {
		val user = UserDao.getOneByUsername(username) ?: return@transaction null
		val receiverId = user.id.value
		val labels = UserLabelDao.getListByUserId(receiverId).map {
			LabelDTO(it.id.value, it.label, it.color)
		}
		val pair = if (userId == receiverId) {
			RelationshipDTO.OWNER to false
		} else {
			val isFriend = FriendRelationshipDao.isFriend(userId, receiverId)
			val relationship = if (isFriend) RelationshipDTO.FRIEND else RelationshipDTO.STRANGER
			val isAlreadySend = FriendRequestDao.isAlreadySend(userId, receiverId)
			relationship to isAlreadySend
		}
		SearchUserDTO(
			userId = user.id.value,
			username = user.username,
			nickname = user.nickname,
			avatarUrl = user.avatarUrl,
			labels = labels,
			relationship = pair.first,
			isAlreadySend = pair.second
		)
	} ?: return@get call.respondOK(SearchStatus.NOT_FOUND)
	
	call.respondOK(searchUser, SearchStatus.SUCCESS)
}

@Serializable
private data class SearchUserDTO(
	val userId: Int,
	val username: String,
	val nickname: String?,
	val avatarUrl: String?,
	val labels: List<LabelDTO>,
	val relationship: RelationshipDTO,
	val isAlreadySend: Boolean
)

@Serializable
private data class LabelDTO(
	val id: Int,
	val label: String,
	val color: String,
)

private enum class RelationshipDTO {
	FRIEND,
	OWNER,
	STRANGER
}

/**
 * 120x
 */
private enum class SearchStatus(
	override val msg: String,
	override val code: Int
) : NoStatus {
	SUCCESS("搜索成功", 0),
	USERNAME_NOT_EMPTY("用户名不能为空", 1200),
	NOT_FOUND("未搜索到该用户", 1201)
}