package com.nocircle.server.app.routes.label

import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.app.plugins.LabelRouteContext
import com.nocircle.server.common.expends.getDisplayLength
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.AuthContext
import com.nocircle.server.common.routes.getPrincipal
import com.nocircle.server.app.code.NoCode
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 添加标签
 */
context(_: LabelRouteContext, _: AuthContext)
fun Route.addLabel() = post("add") {
	val userId = call.getPrincipal().userId
	val parameters = call.receiveParameters()
	val label: String by parameters
	val color: String by parameters
	val status = transaction {
		val displayLength = label.getDisplayLength()
		if (displayLength == 0) {
			return@transaction NoCode.LABEL_ADD_EMPTY
		}
		val labels = UserLabelDao.getListByUserId(userId)
		if (labels.size >= MAX_COUNT) {
			return@transaction NoCode.LABEL_ADD_TOTAL_LIMIT
		}
		if (labels.find { it.label == label } != null) {
			return@transaction NoCode.LABEL_ADD_ALREADY_EXISTS
		}
		val totalLength = labels.sumOf { it.label.getDisplayLength() } + displayLength
		if (totalLength > MAX_TOTAL_DISPLAY_LENGTH) {
			return@transaction NoCode.LABEL_ADD_LENGTH_LIMIT
		}
		val success = UserLabelDao.insertOne(userId, label, color)
		if (success) NoCode.LABEL_ADD_SUCCESS else NoCode.LABEL_ADD_FAILURE
	}
	call.respondOK(status)
}

private const val MAX_COUNT = 4
private const val MAX_TOTAL_DISPLAY_LENGTH = 20