package com.nocircle.server.app.routes.friend

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.GET
import cn.ktorfitx.server.annotation.Principal
import cn.ktorfitx.server.annotation.Query
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRelationshipDao
import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.dao.UserDetailDao
import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.common.expends.create
import com.nocircle.server.common.expends.toKtInstant
import com.nocircle.server.common.model.NoPrincipal
import com.nocircle.shared.model.ApiResult
import com.nocircle.shared.model.friend.FriendDetailDTO
import com.nocircle.shared.model.label.UserLabelDTO
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Authentication
@GET("friend/detail/query")
fun queryFriendDetail(
	@Principal principal: NoPrincipal,
	@Query friendId: Int
): ApiResult<FriendDetailDTO> {
	val userId = principal.userId
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
		ApiResult.create(data, NoCode.FRIEND_DETAIL_QUERY_SUCCESS)
	} else {
		ApiResult.create(NoCode.FRIEND_DETAIL_NOT_FOUND)
	}
}