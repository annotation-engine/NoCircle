package com.nocircle.server.app.routes.friend

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.GET
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRelationshipDao
import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.dao.UserDetailDao
import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.common.expends.toKtInstant
import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.expends.getPrincipal
import com.nocircle.shared.model.friend.FriendDetailDTO
import com.nocircle.shared.model.label.UserLabelDTO
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Authentication
@GET("friend/detail/query")
fun RoutingContext.queryFriendDetail(): ApiResult<FriendDetailDTO> {
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
		val userDetail = UserDetailDao.getOneByUserId(friendId)
		FriendDetailDTO(
			friendId = friendId,
			username = user.username,
			nickname = user.nickname,
			avatarUrl = userDetail.avatarUrl,
			email = userDetail.email,
			signature = userDetail.signature,
			gender = userDetail.gender,
			labels = labels,
			createTime = relationship.createTime.toKtInstant()
		)
	}
	return if (data != null) {
		ApiResult.new(data, NoCode.FRIEND_DETAIL_QUERY_SUCCESS)
	} else {
		ApiResult.new(NoCode.FRIEND_DETAIL_NOT_FOUND)
	}
}