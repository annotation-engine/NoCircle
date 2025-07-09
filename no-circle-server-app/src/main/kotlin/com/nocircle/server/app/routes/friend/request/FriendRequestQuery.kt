package com.nocircle.server.app.routes.friend.request

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.GET
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.dao.UserDetailDao
import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.app.routes.friend.request.FriendRequestType.RECEIVED
import com.nocircle.server.app.routes.friend.request.FriendRequestType.SENT
import com.nocircle.server.common.expends.create
import com.nocircle.server.common.expends.getPrincipal
import com.nocircle.server.common.expends.toKtInstant
import com.nocircle.shared.model.ApiResult
import com.nocircle.shared.model.friend.request.FriendRequestDTO
import com.nocircle.shared.model.label.UserLabelDTO
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.time.ExperimentalTime

/**
 * 好友请求查询
 */
@Authentication
@GET("friend/request/query")
fun RoutingContext.queryFriendRequest(): ApiResult<List<FriendRequestDTO>> {
	val userId = call.getPrincipal().userId
	val type: FriendRequestType by call.parameters
	
	val data = transaction {
		when (type) {
			SENT -> getSentRequests(userId)
			RECEIVED -> getReceivedRequests(userId)
		}
	}
	return ApiResult.create(data, NoCode.FRIEND_REQUEST_QUERY_SUCCESS)
}

/**
 * 获取我发送的请求
 */
@OptIn(ExperimentalTime::class)
private fun getSentRequests(senderId: Int): List<FriendRequestDTO> {
	val requests = FriendRequestDao.getSentRequests(senderId)
	val receiverIds = requests.map { it.receiverId }
	val receiverMap = UserDao.getMapByIds(receiverIds)
	val labels = UserLabelDao.getMapByUserIds(receiverIds)
	val avatarUrlMap = UserDetailDao.getAvatarUrlMapByUserIds(receiverIds)
	return requests.map {
		val user = receiverMap[it.receiverId]!!
		val avatarUrl = avatarUrlMap[it.receiverId]
		val labels = labels[it.receiverId]?.map { label ->
			UserLabelDTO(
				id = label.id.value,
				label = label.label,
				color = label.color,
			)
		} ?: emptyList()
		FriendRequestDTO(
			id = it.id.value,
			targetId = user.id.value,
			username = user.username,
			nickname = user.nickname,
			avatarUrl = avatarUrl,
			status = it.status,
			createTime = it.createTime.toKtInstant(),
			labels = labels
		)
	}
}

/**
 * 获取发送给我的请求
 */
@OptIn(ExperimentalTime::class)
private fun getReceivedRequests(receiverId: Int): List<FriendRequestDTO> {
	val requests = FriendRequestDao.getReceivedRequests(receiverId)
	val senderIds = requests.map { it.senderId }
	val senderMap = UserDao.getMapByIds(senderIds)
	val avatarUrlMap = UserDetailDao.getAvatarUrlMapByUserIds(senderIds)
	val labels = UserLabelDao.getMapByUserIds(senderIds)
	return requests.map {
		val user = senderMap[it.senderId]!!
		val avatarUrl = avatarUrlMap[it.senderId]
		val labels = labels[it.senderId]?.map { label ->
			UserLabelDTO(
				id = label.id.value,
				label = label.label,
				color = label.color,
			)
		} ?: emptyList()
		FriendRequestDTO(
			id = it.id.value,
			targetId = user.id.value,
			username = user.username,
			nickname = user.nickname,
			avatarUrl = avatarUrl,
			status = it.status,
			createTime = it.createTime.toKtInstant(),
			labels = labels
		)
	}
}

private enum class FriendRequestType {
	SENT,
	RECEIVED
}