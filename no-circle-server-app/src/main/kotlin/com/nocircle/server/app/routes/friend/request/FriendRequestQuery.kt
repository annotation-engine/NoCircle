package com.nocircle.server.app.routes.friend.request

import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.app.plugins.FriendContext
import com.nocircle.server.app.tables.FriendRequests
import com.nocircle.server.common.expends.formatToShanghai
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.noPrincipal
import com.nocircle.server.common.model.respondDTO
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 好友请求查询
 */
context(_: FriendContext)
fun Route.getRequestQuery() = get("request/query") {
	val userId = call.noPrincipal!!.userId
	val data = transaction {
		val sentRequests = getSentRequests(userId)
		val receivedRequests = getReceivedRequests(userId)
		FriendRequestDTO(sentRequests, receivedRequests)
	}
	call.respondDTO(data, RequestQueryStatus.SUCCESS)
}

/**
 * 获取我发送的请求
 */
private fun getSentRequests(senderId: Int): List<RequestDTO> {
	val sentFriendRequests = FriendRequestDao.getListBySenderId(senderId)
	val sentReceiverIds = sentFriendRequests.map { it.receiverId }
	val sentUsers = UserDao.getListByIds(sentReceiverIds)
	val sentUserLabelMap = UserLabelDao.getMapByUserIds(sentReceiverIds)
	return sentFriendRequests.map {
		val user = sentUsers.first { user ->
			user.id.value == it.receiverId
		}
		val labels = sentUserLabelMap[it.receiverId]?.map { label ->
			LabelDTO(label.label, label.color)
		} ?: emptyList()
		RequestDTO(
			id = it.id.value,
			username = user.username,
			nickname = user.nickname,
			avatarUrl = user.avatarUrl,
			status = it.status,
			labels = labels,
			updateTime = it.updateTime.formatToShanghai()
		)
	}
}

/**
 * 获取发送给我的请求
 */
private fun getReceivedRequests(receiverId: Int): List<RequestDTO> {
	val receivedFriendRequests = FriendRequestDao.getListByReceiverId(receiverId)
	val receivedSenderIds = receivedFriendRequests.map { it.senderId }
	val receivedUsers = UserDao.getListByIds(receivedSenderIds)
	val receivedUserLabelMap = UserLabelDao.getMapByUserIds(receivedSenderIds)
	return receivedFriendRequests.map {
		val user = receivedUsers.first { user ->
			user.id.value == it.senderId
		}
		val labels = receivedUserLabelMap[it.senderId]?.map { label ->
			LabelDTO(label.label, label.color)
		} ?: emptyList()
		RequestDTO(
			id = it.id.value,
			username = user.username,
			nickname = user.nickname,
			avatarUrl = user.avatarUrl,
			status = it.status,
			labels = labels,
			updateTime = it.updateTime.formatToShanghai()
		)
	}
}

@Serializable
private data class FriendRequestDTO(
	val sentRequests: List<RequestDTO>,
	val receivedRequests: List<RequestDTO>
)

@Serializable
private data class RequestDTO(
	val id: Int,
	val username: String,
	val nickname: String?,
	val avatarUrl: String?,
	val status: FriendRequests.Status,
	val labels: List<LabelDTO>,
	val updateTime: String
)

@Serializable
private data class LabelDTO(
	val label: String,
	val color: String,
)

private enum class RequestQueryStatus(
	override val msg: String,
	override val code: Int
) : NoStatus {
	SUCCESS("查询成功", 0)
}