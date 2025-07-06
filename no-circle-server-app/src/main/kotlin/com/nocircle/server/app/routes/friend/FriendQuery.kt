package com.nocircle.server.app.routes.friend

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.GET
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRelationshipDao
import com.nocircle.server.app.dao.UserDetailDao
import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.expends.getPrincipal
import com.nocircle.shared.model.friend.FriendDTO
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 查询好友
 */
@Authentication
@GET("friend/query")
fun RoutingContext.queryFriend(): ApiResult<List<FriendDTO>> {
	val userId = call.getPrincipal().userId
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
	return ApiResult.new(data, NoCode.FRIEND_QUERY_SUCCESS)
}