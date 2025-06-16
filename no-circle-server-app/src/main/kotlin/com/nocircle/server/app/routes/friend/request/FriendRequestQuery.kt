package com.nocircle.server.app.routes.friend.request

import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.app.plugins.FriendContext
import com.nocircle.server.app.routes.friend.request.FriendRequestType.RECEIVED
import com.nocircle.server.app.routes.friend.request.FriendRequestType.SENT
import com.nocircle.server.app.tables.FriendRequests
import com.nocircle.server.common.expends.formatToShanghai
import com.nocircle.server.common.exposed.getEnum
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
fun Route.getQueryRequest() = get("request/query") {
	val userId = call.noPrincipal!!.userId
	val type = call.parameters.getEnum<FriendRequestType>("type")
	val data = transaction {
		when (type) {
			SENT -> getSentRequests(userId)
			RECEIVED -> getReceivedRequests(userId)
		}
	}
	call.respondDTO(data, RequestQueryStatus.SUCCESS)
}

/**
 * 获取我发送的请求
 */
private fun getSentRequests(senderId: Int): List<RequestDTO> {
	val requests = FriendRequestDao.getSentRequests(senderId)
	val receiverIds = requests.map { it.receiverId }
	val receivers = UserDao.getListByIds(receiverIds)
	val labels = UserLabelDao.getMapByUserIds(receiverIds)
	return requests.map {
		val user = receivers.first { user ->
			user.id.value == it.receiverId
		}
		val labels = labels[it.receiverId]?.associate { label ->
			label.label to label.color
		} ?: emptyMap()
		RequestDTO(
			id = it.id.value,
			username = user.username,
			nickname = user.nickname,
			avatarUrl = user.avatarUrl,
			status = it.status,
			createTime = it.createTime.formatToShanghai(),
			labels = labels
		)
	}
}

/**
 * 获取发送给我的请求
 */
private fun getReceivedRequests(receiverId: Int): List<RequestDTO> {
	val requests = FriendRequestDao.getReceivedRequests(receiverId)
	val senderIds = requests.map { it.senderId }
	val senders = UserDao.getListByIds(senderIds)
	val labels = UserLabelDao.getMapByUserIds(senderIds)
	return requests.map {
		val user = senders.first { user ->
			user.id.value == it.senderId
		}
		val labels = labels[it.senderId]?.associate { label ->
			label.label to label.color
		} ?: emptyMap()
		RequestDTO(
			id = it.id.value,
			username = user.username,
			nickname = user.nickname,
			avatarUrl = user.avatarUrl,
			status = it.status,
			createTime = it.createTime.formatToShanghai(),
			labels = labels
		)
	}
}

@Serializable
private data class RequestDTO(
	val id: Int,
	val username: String,
	val nickname: String?,
	val avatarUrl: String?,
	val status: FriendRequests.Status,
	val createTime: String,
	val labels: Map<String, String>
)

/**
 * 122x
 */
private enum class RequestQueryStatus(
	override val msg: String,
	override val code: Int
) : NoStatus {
	SUCCESS("查询成功", 0)
}

private enum class FriendRequestType {
	SENT,
	RECEIVED
}