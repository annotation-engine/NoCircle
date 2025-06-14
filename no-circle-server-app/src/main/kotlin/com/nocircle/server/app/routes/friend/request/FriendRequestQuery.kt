package com.nocircle.server.app.routes.friend.request

import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.plugins.FriendContext
import com.nocircle.server.app.tables.FriendRequests
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.noPrincipal
import com.nocircle.server.common.model.respondDTO
import io.ktor.server.routing.*
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

context(_: FriendContext)
fun Route.getRequestQuery() = get("request/query") {
	val userId = call.noPrincipal!!.userId
	val data = transaction {
		val sentFriendRequests = FriendRequestDao.getListBySenderId(userId)
		val sentReceiverIds = sentFriendRequests.map { it.receiverId }
		val sentUsers = UserDao.getListByIds(sentReceiverIds)
		val sentRequests = sentFriendRequests.map {
			val user = sentUsers.first { user ->
				user.id.value == it.receiverId
			}
			SentRequestDTO(
				receiverId = it.receiverId,
				username = user.username,
				nickname = user.nickname,
				avatarUrl = user.avatarUrl,
				status = it.status,
				updateTime = it.updateTime
			)
		}
		
		val receivedFriendRequests = FriendRequestDao.getListByReceiverId(userId)
		val receivedSenderIds = receivedFriendRequests.map { it.senderId }
		val receivedUsers = UserDao.getListByIds(receivedSenderIds)
		val receivedRequests = receivedFriendRequests.map {
			val user = receivedUsers.first { user ->
				user.id.value == it.senderId
			}
			ReceivedRequestDTO(
				senderId = it.receiverId,
				username = user.username,
				nickname = user.nickname,
				avatarUrl = user.avatarUrl,
				status = it.status,
				updateTime = it.updateTime
			)
		}
		FriendRequestDTO(sentRequests, receivedRequests)
	}
	call.respondDTO(data, RequestQueryStatus.SUCCESS)
}

@Serializable
private data class FriendRequestDTO(
	val sentRequests: List<SentRequestDTO>,
	val receivedRequests: List<ReceivedRequestDTO>
)

@Serializable
private data class SentRequestDTO(
	val receiverId: Int,
	val username: String,
	val nickname: String?,
	val avatarUrl: String?,
	val status: FriendRequests.Status,
	val updateTime: Instant
)

@Serializable
private data class ReceivedRequestDTO(
	val senderId: Int,
	val username: String,
	val nickname: String?,
	val avatarUrl: String?,
	val status: FriendRequests.Status,
	val updateTime: Instant
)

private enum class RequestQueryStatus(
	override val msg: String,
	override val code: Int
) : NoStatus {
	SUCCESS("查询成功", 0)
}