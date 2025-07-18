package com.nocircle.server.app.routes.friend

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.GET
import cn.ktorfitx.server.annotation.Principal
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRelationshipDao
import com.nocircle.server.app.dao.UserDetailDao
import com.nocircle.server.common.expends.create
import com.nocircle.server.common.model.NoPrincipal
import com.nocircle.shared.model.ApiResult
import com.nocircle.shared.model.friend.FriendDTO
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 查询好友
 */
@Authentication
@GET("friend/query")
fun queryFriend(
	@Principal principal: NoPrincipal
): ApiResult<List<FriendDTO>> {
	val userId = principal.userId
	val data = transaction {
		val friends = FriendRelationshipDao.getFriendsByUserId(userId)
		val friendIds = friends.map { it.friendId.value }
		val avatarUrlMap = UserDetailDao.getAvatarUrlMapByUserIds(friendIds)
		friends.map {
			val friendId = it.friendId.value
			FriendDTO(
				friendId = friendId,
				username = it.username,
				nickname = it.nickname,
				avatarUrl = avatarUrlMap[friendId],
				pinyin = it.pinyin
			)
		}
	}
	return ApiResult.create(data, NoCode.FRIEND_QUERY_SUCCESS)
}