package com.nocircle.server.app.routes.friend.request

import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.plugins.FriendRouteGroup
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.Authorized
import com.nocircle.server.common.routes.getPrincipal
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 删除好友请求
 */
context(_: FriendRouteGroup, _: Authorized)
fun Route.deleteFriendRequest() = post("request/delete") {
	val userId = call.getPrincipal().userId
	val parameters = call.receiveParameters()
	val id: Int by parameters
	val targetId: Int by parameters
	
	val success = transaction {
		FriendRequestDao.deleteOne(userId, targetId)
	}
	val code = if (success) NoCode.FRIEND_REQUEST_DELETE_SUCCESS else NoCode.FRIEND_REQUEST_DELETE_FAILURE
	call.respondOK(code)
}