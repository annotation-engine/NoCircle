package com.nocircle.server.app.routes.friend.request

import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRelationshipDao
import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.plugins.FriendRouteGroup
import com.nocircle.server.app.tables.FriendRequests
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.Authorized
import com.nocircle.server.common.routes.getPrincipal
import com.nocircle.server.common.websockets.sendToReceiver
import com.nocircle.shared.websocket.WebSocketType
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
	
	val code = transaction {
		var success = FriendRequestDao.updateOne(id, targetId, userId, FriendRequests.Status.AGREED)
		if (!success) return@transaction NoCode.FRIEND_REQUEST_AGREE_FAILURE
		success = FriendRelationshipDao.insertOne(targetId, userId)
		if (!success) return@transaction NoCode.FRIEND_REQUEST_AGREE_FAILURE
		FriendRequestDao.deleteOne(id, userId, targetId)
		NoCode.FRIEND_REQUEST_AGREE_SUCCESS
	}
	if (code == NoCode.FRIEND_REQUEST_AGREE_SUCCESS) {
		sendToReceiver(WebSocketType.FRIEND_SENT_REQUEST, userId, targetId)
		sendToReceiver(WebSocketType.FRIEND_RECEIVED_REQUEST_COUNT, userId, userId)
	}
	call.respondOK(code)
}