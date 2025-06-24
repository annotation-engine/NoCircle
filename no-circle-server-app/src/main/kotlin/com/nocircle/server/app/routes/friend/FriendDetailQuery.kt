package com.nocircle.server.app.routes.friend

import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRelationshipDao
import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.app.plugins.FriendRouteContext
import com.nocircle.server.common.expends.toShanghaiLocalDateTime
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.AuthContext
import com.nocircle.server.common.routes.getPrincipal
import com.nocircle.shared.model.friend.FriendDetailDTO
import com.nocircle.shared.model.label.UserLabelDTO
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

context(_: FriendRouteContext, _: AuthContext)
fun Route.queryFriendDetail() = get("detail/query") {
	val userId = call.getPrincipal().userId
	val friendId: Int by call.queryParameters
	val data = transaction {
		val user = UserDao.getOneById(friendId) ?: return@transaction null
		val friendId = user.id.value
		val relationship = FriendRelationshipDao.getOneByUserIdAndFriendId(userId, friendId)
			?: return@transaction null
		val labels = UserLabelDao.getListByUserId(friendId).map {
			UserLabelDTO(
				it.id.value,
				label = it.label,
				color = it.color
			)
		}
		FriendDetailDTO(
			friendId = friendId,
			username = user.username,
			nickname = user.nickname,
			avatarUrl = user.avatarUrl,
			labels = labels,
			createTime = relationship.createTime.toShanghaiLocalDateTime()
		)
	}
	if (data != null) {
		call.respondOK(data, NoCode.FRIEND_DETAIL_QUERY_SUCCESS)
	} else {
		call.respondOK(NoCode.FRIEND_DETAIL_NOT_FOUND)
	}
}