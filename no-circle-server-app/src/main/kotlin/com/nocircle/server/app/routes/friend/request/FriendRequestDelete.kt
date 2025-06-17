package com.nocircle.server.app.routes.friend.request

import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.plugins.FriendRouteGroup
import com.nocircle.server.app.routes.friend.request.RequestDeleteStatus.FAILURE
import com.nocircle.server.app.routes.friend.request.RequestDeleteStatus.SUCCESS
import com.nocircle.server.common.model.NoStatus
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
fun Route.postDeleteRequest() = post("request/delete") {
	val userId = call.getPrincipal().userId
	val parameters = call.receiveParameters()
	val id: Int by parameters
	val targetId: Int by parameters
	
	val success = transaction {
		FriendRequestDao.deleteOne(id, userId, targetId)
	}
	val status = if (success) SUCCESS else FAILURE
	call.respondOK(status)
}

/**
 * 124x
 */
private enum class RequestDeleteStatus(
	override val msg: String,
	override val code: Int
) : NoStatus {
	SUCCESS("删除成功", 0),
	FAILURE("删除失败", 1240)
}