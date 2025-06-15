package com.nocircle.server.app.routes.friend.request

import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.plugins.FriendContext
import com.nocircle.server.app.tables.FriendRequests
import com.nocircle.server.common.exposed.getInt
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.noPrincipal
import com.nocircle.server.common.model.respondDTO
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 取消好友请求
 */
context(_: FriendContext)
fun Route.postCancelRequest() = post("/request/cancel") {
	val userId = call.noPrincipal!!.userId
	val id = call.receiveParameters().getInt("id")
	val success = transaction {
		FriendRequestDao.updateOneByIdAndSenderId(id, userId, FriendRequests.Status.CANCELED)
	}
	call.respondDTO(if (success) RequestCancelStatus.SUCCESS else RequestCancelStatus.FAILURE)
}

/**
 * 123x
 */
private enum class RequestCancelStatus(
	override val msg: String,
	override val code: Int
) : NoStatus {
	SUCCESS("取消成功", 0),
	FAILURE("取消失败", 1230)
}