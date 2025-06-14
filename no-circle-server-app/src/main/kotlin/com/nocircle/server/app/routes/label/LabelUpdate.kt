package com.nocircle.server.app.routes.label

import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.app.plugins.LabelContext
import com.nocircle.server.common.expends.getDisplayLength
import com.nocircle.server.common.exposed.getInt
import com.nocircle.server.common.exposed.getString
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.noPrincipal
import com.nocircle.server.common.model.respondDTO
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 更新标签
 */
context(_: LabelContext)
fun Route.postUpdate() = post("update") {
	val userId = call.noPrincipal!!.userId
	val parameters = call.receiveParameters()
	val id = parameters.getInt("id")
	val label = parameters.getString("label")
	val color = parameters.getString("color")
	val status = transaction {
		val displayLength = label.getDisplayLength()
		if (displayLength == 0) {
			return@transaction UpdateStatus.EMPTY
		}
		val userLabel = UserLabelDao.getOneById(id)
			?: return@transaction UpdateStatus.NOT_FOUND
		
		if (userLabel.label == label && userLabel.color == color) {
			return@transaction UpdateStatus.NO_CHANGE
		}
		
		val labels = UserLabelDao.getListByUserIdAndNeqId(userId, id)
		if (labels.find { it.label == label } != null) {
			return@transaction UpdateStatus.ALREADY_EXISTS
		}
		val totalLength = labels.sumOf { it.label.getDisplayLength() } + displayLength
		if (totalLength > MAX_TOTAL_DISPLAY_LENGTH) {
			return@transaction UpdateStatus.LENGTH_LIMIT
		}
		val success = UserLabelDao.updateOne(userId, id, label, color)
		if (success) UpdateStatus.SUCCESS else UpdateStatus.FAILURE
	}
	call.respondDTO(status)
}

private const val MAX_TOTAL_DISPLAY_LENGTH = 20

/**
 * 113x
 */
private enum class UpdateStatus(
	override val msg: String,
	override val code: Int
) : NoStatus {
	SUCCESS("标签修改成功", 0),
	FAILURE("标签修改失败", 1130),
	NOT_FOUND("未找到标签", 1131),
	NO_CHANGE("标签无需修改", 1132),
	ALREADY_EXISTS("标签已存在", 1133),
	LENGTH_LIMIT("超过总长度限制", 1134),
	EMPTY("标签不能为空", 1135)
}