package com.nocircle.server.app.routes.friend

import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRelationshipDao
import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.plugins.FriendRouteGroup
import com.nocircle.server.common.expends.toShanghaiLocalDateTime
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.Authorized
import com.nocircle.server.common.routes.getPrincipal
import com.nocircle.shared.model.PageResult
import com.nocircle.shared.model.friend.FriendDTO
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 查询好友
 */
context(_: FriendRouteGroup, _: Authorized)
fun Route.queryFriend() = get("query") {
	val userId = call.getPrincipal().userId
	val parameters = call.parameters
	val pageNumber: Int by parameters
	val pageSize: Int by parameters
	val orderType: FriendRelationshipDao.OrderType by parameters
	if (pageNumber <= 0 || pageSize <= 0) {
		call.respondOK(NoCode.FRIEND_QUERY_PARAMETER_ERROR)
		return@get
	}
	
	val result = transaction {
		val friendRelationships = FriendRelationshipDao.getListByUserIdAndPageAndSize(userId, pageNumber, pageSize, orderType)
		val friendIds = friendRelationships.map { it.friendId }
		val friends = UserDao.getListByIds(friendIds)
		val items = friendRelationships.map { relationship ->
			val friend = friends.first { it.id.value == relationship.friendId }
			FriendDTO(
				userId = friend.id.value,
				username = friend.username,
				nickname = friend.nickname,
				avatarUrl = friend.avatarUrl,
				pinyin = friend.pinyin,
				createdTime = relationship.createTime.toShanghaiLocalDateTime()
			)
		}
		val total = FriendRelationshipDao.getCountByUserId(userId)
		PageResult(items, total)
	}
	call.respondOK(result, NoCode.FRIEND_QUERY_SUCCESS)
}