package com.nocircle.server.app.routes.label

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.POST
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.common.expends.create
import com.nocircle.server.common.expends.getDisplayLength
import com.nocircle.server.common.expends.getPrincipal
import com.nocircle.shared.model.ApiResult
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 更新标签
 */
@Authentication
@POST("label/update")
suspend fun RoutingContext.updateLabel(): ApiResult<Unit> {
	val userId = call.getPrincipal().userId
	val parameters = call.receiveParameters()
	val id: Int by parameters
	val label: String by parameters
	val color: String by parameters
	
	val code = transaction {
		val displayLength = label.getDisplayLength()
		if (displayLength == 0) {
			return@transaction NoCode.LABEL_UPDATE_EMPTY
		}
		val userLabel = UserLabelDao.getOneById(id)
			?: return@transaction NoCode.LABEL_UPDATE_NOT_FOUND
		
		if (userLabel.label == label && userLabel.color == color) {
			return@transaction NoCode.LABEL_UPDATE_NO_CHANGE
		}
		
		val labels = UserLabelDao.getListByUserIdAndNeqId(userId, id)
		if (labels.find { it.label == label } != null) {
			return@transaction NoCode.LABEL_UPDATE_ALREADY_EXISTS
		}
		val totalLength = labels.sumOf { it.label.getDisplayLength() } + displayLength
		if (totalLength > MAX_TOTAL_DISPLAY_LENGTH) {
			return@transaction NoCode.LABEL_UPDATE_LENGTH_LIMIT
		}
		val success = UserLabelDao.updateOne(userId, id, label, color)
		if (success) NoCode.LABEL_UPDATE_SUCCESS else NoCode.LABEL_UPDATE_FAILURE
	}
	return ApiResult.create(code)
}

private const val MAX_TOTAL_DISPLAY_LENGTH = 20