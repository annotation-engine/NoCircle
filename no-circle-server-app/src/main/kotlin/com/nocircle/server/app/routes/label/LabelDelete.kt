package com.nocircle.server.app.routes.label

import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.app.plugins.LabelRouteGroup
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.Authorized
import com.nocircle.server.common.routes.getPrincipal
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 删除标签
 */
context(_: LabelRouteGroup, _: Authorized)
fun Route.postDeleteLabel() = post("delete") {
	val userId = call.getPrincipal().userId
	val id: Int by call.receiveParameters()
	val success = transaction {
		UserLabelDao.deleteOne(userId, id)
	}
	val status = if (success) DeleteStatus.SUCCESS else DeleteStatus.FAILURE
	call.respondOK(status)
}

/**
 * 111x
 */
private enum class DeleteStatus(
	override val msg: String,
	override val code: Int
) : NoStatus {
	SUCCESS("标签删除成功", 0),
	FAILURE("标签删除失败", 1110)
}