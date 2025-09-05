package com.nocircle.server.app.routes.label

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.Field
import cn.ktorfitx.server.annotation.POST
import cn.ktorfitx.server.annotation.Principal
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.common.expends.create
import com.nocircle.server.common.expends.getDisplayLength
import com.nocircle.server.common.exposed.tx
import com.nocircle.server.common.model.NoPrincipal
import com.nocircle.shared.model.ApiResult

/**
 * 更新标签
 */
@Authentication
@POST("label/update")
suspend fun updateLabel(
	@Principal principal: NoPrincipal,
	@Field id: Int,
	@Field label: String,
	@Field color: String
): ApiResult<Unit> {
	val userId = principal.userId
	val code = tx {
		val displayLength = label.getDisplayLength()
		if (displayLength == 0) {
			return@tx NoCode.LABEL_UPDATE_EMPTY
		}
		val userLabel = UserLabelDao.getOneById(id)
			?: return@tx NoCode.LABEL_UPDATE_NOT_FOUND
		
		if (userLabel.label == label && userLabel.color == color) {
			return@tx NoCode.LABEL_UPDATE_NO_CHANGE
		}
		
		val labels = UserLabelDao.getListByUserIdAndNeqId(userId, id)
		if (labels.find { it.label == label } != null) {
			return@tx NoCode.LABEL_UPDATE_ALREADY_EXISTS
		}
		val totalLength = labels.sumOf { it.label.getDisplayLength() } + displayLength
		if (totalLength > MAX_TOTAL_DISPLAY_LENGTH) {
			return@tx NoCode.LABEL_UPDATE_LENGTH_LIMIT
		}
		val success = UserLabelDao.updateOne(userId, id, label, color)
		if (success) NoCode.LABEL_UPDATE_SUCCESS else NoCode.LABEL_UPDATE_FAILURE
	}
	return ApiResult.create(code)
}

private const val MAX_TOTAL_DISPLAY_LENGTH = 20