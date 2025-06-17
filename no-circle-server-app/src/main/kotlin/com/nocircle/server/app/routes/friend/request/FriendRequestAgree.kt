package com.nocircle.server.app.routes.friend.request

import com.nocircle.server.app.dao.FriendRelationshipDao
import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.plugins.FriendRouteGroup
import com.nocircle.server.app.plugins.WebSocketType
import com.nocircle.server.app.tables.FriendRequests
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.Authorized
import com.nocircle.server.common.routes.getPrincipal
import com.nocircle.server.common.websockets.getSession
import com.nocircle.server.common.websockets.sendMessage
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 同意好友请求
 */
context(_: FriendRouteGroup, _: Authorized)
fun Route.postAgreeRequest() = post("request/agree") {
	val userId = call.getPrincipal().userId
	val parameters = call.receiveParameters()
	val id: Int by parameters
	val targetId: Int by parameters
	
	val status = transaction {
		var success = FriendRequestDao.updateOne(id, targetId, userId, FriendRequests.Status.AGREED)
		if (!success) return@transaction RequestAgreeStatus.FAILURE
		success = FriendRelationshipDao.insertOne(targetId, userId)
		if (!success) return@transaction RequestAgreeStatus.FAILURE
		FriendRequestDao.deleteOne(id, userId, targetId)
		RequestAgreeStatus.SUCCESS
	}
	if (status == RequestAgreeStatus.SUCCESS) {
		getSession(targetId)?.sendMessage(
			type = WebSocketType.REFRESH_FRIEND_RECEIVED_REQUEST,
			senderId = userId
		)
	}
	call.respondOK(status)
}

/**
 * 125x
 */
private enum class RequestAgreeStatus(
	override val msg: String,
	override val code: Int
) : NoStatus {
	SUCCESS("好友添加成功", 0),
	FAILURE("好友添加失败", 1250)
}