package com.nocircle.server.app.routes.friend

import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRelationshipDao
import com.nocircle.server.app.plugins.FriendRouteContext
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.AuthContext
import com.nocircle.server.common.routes.getPrincipal
import com.nocircle.shared.model.friend.FriendDTO
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 查询好友
 */
context(_: FriendRouteContext, _: AuthContext)
fun Route.queryFriend() = get("query") {
	val userId = call.getPrincipal().userId
	val data = transaction {
		FriendRelationshipDao.getFriendsByUserId(userId).map {
			FriendDTO(
				friendId = it.friendId.value,
				username = it.username,
				nickname = it.nickname,
				avatarUrl = it.avatarUrl,
				pinyin = it.pinyin
			)
		}
	}
	call.respondOK(data, NoCode.FRIEND_QUERY_SUCCESS)
}