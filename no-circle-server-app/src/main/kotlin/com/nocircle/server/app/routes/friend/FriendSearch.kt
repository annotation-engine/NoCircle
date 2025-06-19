package com.nocircle.server.app.routes.friend

import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRelationshipDao
import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.app.plugins.FriendRouteGroup
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.Authorized
import com.nocircle.server.common.routes.getPrincipal
import com.nocircle.shared.model.friend.FriendSearchDTO
import com.nocircle.shared.model.friend.FriendSearchDTO.RelationshipDTO.*
import com.nocircle.shared.model.label.LabelDTO
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 搜索好友
 */
context(_: FriendRouteGroup, _: Authorized)
fun Route.searchFriend() = get("search") {
	val userId = call.getPrincipal().userId
	val username: String by call.queryParameters
	if (username.isBlank()) {
		return@get call.respondOK(NoCode.FRIEND_SEARCH_USERNAME_NOT_EMPTY)
	}
	val searchUser = transaction {
		val user = UserDao.getOneByUsername(username) ?: return@transaction null
		val receiverId = user.id.value
		val labels = UserLabelDao.getListByUserId(receiverId).map {
			LabelDTO(it.id.value, it.label, it.color)
		}
		val relationship = if (userId == receiverId) OWNER else {
			val isFriend = FriendRelationshipDao.isFriend(userId, receiverId)
			if (isFriend) FRIEND else STRANGER
		}
		val isAlreadySend = if (userId == receiverId) false else {
			FriendRequestDao.isAlreadySend(userId, receiverId)
		}
		FriendSearchDTO(
			userId = user.id.value,
			username = user.username,
			nickname = user.nickname,
			avatarUrl = user.avatarUrl,
			labels = labels,
			relationship = relationship,
			isAlreadySend = isAlreadySend
		)
	} ?: return@get call.respondOK(NoCode.FRIEND_SEARCH_NOT_FOUND)
	
	call.respondOK(searchUser, NoCode.FRIEND_SEARCH_SUCCESS)
}