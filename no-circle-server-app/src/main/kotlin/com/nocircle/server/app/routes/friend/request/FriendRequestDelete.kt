package com.nocircle.server.app.routes.friend.request

import com.nocircle.server.app.dao.FriendRequestDao
import com.nocircle.server.app.plugins.FriendContext
import com.nocircle.server.common.exposed.getInt
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.noPrincipal
import com.nocircle.server.common.model.respondDTO
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 删除好友请求
 */
context(_: FriendContext)
fun Route.postDeleteRequest() = post("request/delete") {
	val userId = call.noPrincipal!!.userId
	val id = call.receiveParameters().getInt("id")
	val success = transaction {
		FriendRequestDao.deleteByIdAndSenderId(id, userId)
	}
	call.respondDTO(if (success) RequestDeleteStatus.SUCCESS else RequestDeleteStatus.FAILURE)
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