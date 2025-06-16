package com.nocircle.server.app.routes.label

import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.app.plugins.LabelContext
import com.nocircle.server.common.exposed.getInt
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.noPrincipal
import com.nocircle.server.common.model.respondDTO
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 删除标签
 */
context(_: LabelContext)
fun Route.postDeleteLabel() = post("delete") {
	val userId = call.noPrincipal!!.userId
	val id = call.receiveParameters().getInt("id")
	val success = transaction {
		UserLabelDao.deleteOne(userId, id)
	}
	val status = if (success) DeleteStatus.SUCCESS else DeleteStatus.FAILURE
	call.respondDTO(status)
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