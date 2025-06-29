package com.nocircle.server.app.routes.user

import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.*
import com.nocircle.server.app.plugins.UserRouteContext
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.AuthContext
import com.nocircle.server.common.routes.getPrincipal
import com.nocircle.shared.model.label.UserLabelDTO
import com.nocircle.shared.model.user.UserSearchDTO
import com.nocircle.shared.model.user.UserSearchDTO.RelationshipDTO.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 搜索用户
 */
context(_: UserRouteContext, _: AuthContext)
fun Route.searchUser() = get("search") {
	val userId = call.getPrincipal().userId
	val username: String by call.queryParameters
	if (username.isBlank()) {
		return@get call.respondOK(NoCode.USER_SEARCH_USERNAME_NOT_EMPTY)
	}
	val searchUser = transaction {
		val user = UserDao.getOneByUsername(username) ?: return@transaction null
		val receiverId = user.id.value
		val labels = UserLabelDao.getListByUserId(receiverId).map {
			UserLabelDTO(
				id = it.id.value,
				label = it.label,
				color = it.color
			)
		}
		val relationship = if (userId == receiverId) OWNER else {
			val isFriend = FriendRelationshipDao.isFriend(userId, receiverId)
			if (isFriend) FRIEND else STRANGER
		}
		val isAlreadySend = if (userId == receiverId) false else {
			FriendRequestDao.isAlreadySend(userId, receiverId)
		}
		val avatarUrl = UserDetailDao.getAvatarUrlByUserId(receiverId)
		UserSearchDTO(
			userId = user.id.value,
			username = user.username,
			nickname = user.nickname,
			avatarUrl = avatarUrl,
			labels = labels,
			relationship = relationship,
			isAlreadySend = isAlreadySend
		)
	} ?: return@get call.respondOK(NoCode.USER_SEARCH_NOT_FOUND)
	
	call.respondOK(searchUser, NoCode.USER_SEARCH_SUCCESS)
}