package com.nocircle.server.app.routes.user

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.GET
import cn.ktorfitx.server.annotation.Principal
import cn.ktorfitx.server.annotation.Query
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.*
import com.nocircle.server.common.expends.create
import com.nocircle.server.common.model.NoPrincipal
import com.nocircle.shared.model.ApiResult
import com.nocircle.shared.model.label.UserLabelDTO
import com.nocircle.shared.model.user.UserSearchDTO
import com.nocircle.shared.model.user.UserSearchDTO.RelationshipDTO.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 搜索用户
 */
@Authentication
@GET("user/search")
fun searchUser(
	@Principal principal: NoPrincipal,
	@Query username: String,
): ApiResult<UserSearchDTO> {
	val userId = principal.userId
	if (username.isBlank()) {
		return ApiResult.create(NoCode.USER_SEARCH_USERNAME_NOT_EMPTY)
	}
	val data = transaction {
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
	}
	return if (data != null) {
		ApiResult.create(data, NoCode.USER_SEARCH_NOT_FOUND)
	} else {
		ApiResult.create(NoCode.USER_SEARCH_SUCCESS)
	}
}