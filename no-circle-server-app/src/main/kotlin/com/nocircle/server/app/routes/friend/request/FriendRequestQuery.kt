package com.nocircle.server.app.routes.friend.request

import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.app.plugins.FriendRouteContext
import com.nocircle.server.app.routes.friend.request.FriendRequestType.RECEIVED
import com.nocircle.server.app.routes.friend.request.FriendRequestType.SENT
import com.nocircle.server.app.tables.FriendRequests
import com.nocircle.server.common.expends.formatToShanghai
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.AuthContext
import com.nocircle.server.common.routes.getPrincipal
import com.nocircle.shared.model.friend.request.FriendRequestDTO
import com.nocircle.shared.model.label.UserLabelDTO
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 好友请求查询
 */
context(_: FriendRouteContext, _: AuthContext)
fun Route.queryFriendRequest() = get("request/query") {
	val userId = call.getPrincipal().userId
	val type: FriendRequestType by call.parameters
	
	val data = transaction {
		when (type) {
			SENT -> getSentRequests(userId)
			RECEIVED -> getReceivedRequests(userId)
		}
	}
	call.respondOK(data, NoCode.FRIEND_REQUEST_QUERY_SUCCESS)
}

/**
 * 获取我发送的请求
 */
private fun getSentRequests(senderId: Int): List<FriendRequestDTO> {
	val requests = FriendRequestDao.getSentRequests(senderId)
	val receiverIds = requests.map { it.receiverId }
	val receivers = UserDao.getListByIds(receiverIds)
	val labels = UserLabelDao.getMapByUserIds(receiverIds)
	return requests.map {
		val user = receivers.first { user ->
			user.id.value == it.receiverId
		}
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
			avatarUrl = user.avatarUrl,
			status = it.status.toDTOStatus(),
			createTime = it.createTime.formatToShanghai(),
			labels = labels
		)
	}
}

/**
 * 获取发送给我的请求
 */
private fun getReceivedRequests(receiverId: Int): List<FriendRequestDTO> {
	val requests = FriendRequestDao.getReceivedRequests(receiverId)
	val senderIds = requests.map { it.senderId }
	val senders = UserDao.getListByIds(senderIds)
	val labels = UserLabelDao.getMapByUserIds(senderIds)
	return requests.map {
		val user = senders.first { user ->
			user.id.value == it.senderId
		}
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
			avatarUrl = user.avatarUrl,
			status = it.status.toDTOStatus(),
			createTime = it.createTime.formatToShanghai(),
			labels = labels
		)
	}
}

private fun FriendRequests.Status.toDTOStatus(): FriendRequestDTO.Status {
	return when (this) {
		FriendRequests.Status.AGREED -> FriendRequestDTO.Status.AGREED
		FriendRequests.Status.REJECTED -> FriendRequestDTO.Status.REJECTED
		FriendRequests.Status.PENDING -> FriendRequestDTO.Status.PENDING
		FriendRequests.Status.CANCELED -> FriendRequestDTO.Status.CANCELED
	}
}

private enum class FriendRequestType {
	SENT,
	RECEIVED
}