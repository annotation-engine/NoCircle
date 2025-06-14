package com.nocircle.server.app.routes.label

import com.nocircle.server.app.plugins.LabelContext
import com.nocircle.server.app.tables.user.UserLabels
import com.nocircle.server.common.exposed.getInt
import com.nocircle.server.common.model.Status
import com.nocircle.server.common.model.noPrincipal
import com.nocircle.server.common.model.respond
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 删除标签
 */
context(_: LabelContext)
fun Route.postDelete() = post("delete") {
	val userId = call.noPrincipal!!.userId
	val id = call.receiveParameters().getInt("id")
	val success = transaction {
		UserLabels.deleteOne(userId, id)
	}
	val status = if (success) DeleteStatus.SUCCESS else DeleteStatus.FAILURE
	call.respond(status)
}

/**
 * 111x
 */
private enum class DeleteStatus(
	override val msg: String,
	override val code: Int
) : Status {
	SUCCESS("标签删除成功", 0),
	FAILURE("标签删除失败", 1110)
}