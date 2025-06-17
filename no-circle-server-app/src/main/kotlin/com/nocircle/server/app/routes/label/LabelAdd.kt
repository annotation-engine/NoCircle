package com.nocircle.server.app.routes.label

import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.app.plugins.LabelRouteGroup
import com.nocircle.server.common.expends.getDisplayLength
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.Authorized
import com.nocircle.server.common.routes.getPrincipal
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 添加标签
 */
context(_: LabelRouteGroup, _: Authorized)
fun Route.postAddLabel() = post("add") {
	val userId = call.getPrincipal().userId
	val parameters = call.receiveParameters()
	val label: String by parameters
	val color: String by parameters
	val status = transaction {
		val displayLength = label.getDisplayLength()
		if (displayLength == 0) {
			return@transaction AddStatus.EMPTY
		}
		val labels = UserLabelDao.getListByUserId(userId)
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
		val success = UserLabelDao.insertOne(userId, label, color)
		if (success) AddStatus.SUCCESS else AddStatus.FAILURE
	}
	call.respondOK(status)
}

private const val MAX_COUNT = 4
private const val MAX_TOTAL_DISPLAY_LENGTH = 20

/**
 * 110x
 */
private enum class AddStatus(
	override val msg: String,
	override val code: Int
) : NoStatus {
	SUCCESS("标签添加成功", 0),
	FAILURE("标签添加失败", 1100),
	ALREADY_EXISTS("标签已存在", 1101),
	TOTAL_LIMIT("超过最大数量限制", 1102),
	LENGTH_LIMIT("超过总长度限制", 1103),
	EMPTY("标签不能为空", 1104)
}