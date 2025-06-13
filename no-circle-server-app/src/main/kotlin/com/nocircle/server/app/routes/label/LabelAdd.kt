package com.nocircle.server.app.routes.label

import com.nocircle.server.app.plugins.LabelContext
import com.nocircle.server.app.tables.user.UserLabels
import com.nocircle.server.common.expends.getDisplayLength
import com.nocircle.server.common.exposed.getString
import com.nocircle.server.common.model.Status
import com.nocircle.server.common.model.noPrincipal
import com.nocircle.server.common.model.respond
import com.nocircle.server.common.routes.AuthenticateContext
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 添加标签
 */
context(_: LabelContext, _: AuthenticateContext)
fun Route.postAdd() = post("add") {
	val userId = call.noPrincipal!!.userId
	val parameters = call.receiveParameters()
	val label = parameters.getString("label")
	val color = parameters.getString("color")
	val status = transaction {
		val displayLength = label.getDisplayLength()
		if (displayLength == 0) {
			return@transaction AddStatus.EMPTY
		}
		val labels = UserLabels.getListByUserId(userId)
		if (labels.size >= MAX_COUNT) {
			return@transaction AddStatus.TOTAL_LIMIT
		}
		if (labels.find { it.label == label } != null) {
			return@transaction AddStatus.ALREADY_EXISTS
		}
		val totalLength = labels.sumOf { it.label.getDisplayLength() } + displayLength
		if (totalLength > MAX_TOTAL_DISPLAY_LENGTH) {
			return@transaction AddStatus.LENGTH_LIMIT
		}
		val success = UserLabels.insertOne(userId, label, color)
		if (success) AddStatus.SUCCESS else AddStatus.FAILURE
	}
	call.respond(status)
}

private const val MAX_COUNT = 4
private const val MAX_TOTAL_DISPLAY_LENGTH = 20

/**
 * 200X
 */
private enum class AddStatus(
	override val msg: String,
	override val code: Int
) : Status {
	SUCCESS("标签添加成功", 0),
	FAILURE("标签添加失败", 2000),
	ALREADY_EXISTS("标签已存在", 2001),
	TOTAL_LIMIT("超过最大数量限制", 2002),
	LENGTH_LIMIT("超过总长度限制", 2003),
	EMPTY("标签不能为空", 2004)
}