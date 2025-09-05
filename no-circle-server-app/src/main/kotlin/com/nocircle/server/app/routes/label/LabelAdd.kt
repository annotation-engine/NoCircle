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
 * 添加标签
 */
@Authentication
@POST("label/add")
suspend fun addLabel(
	@Principal principal: NoPrincipal,
	@Field label: String,
	@Field color: String,
): ApiResult<Unit> {
	val userId = principal.userId
	val code = tx {
		val displayLength = label.getDisplayLength()
		if (displayLength == 0) {
			return@tx NoCode.LABEL_ADD_EMPTY
		}
		val labels = UserLabelDao.getListByUserId(userId)
		if (labels.size >= MAX_COUNT) {
			return@tx NoCode.LABEL_ADD_TOTAL_LIMIT
		}
		if (labels.find { it.label == label } != null) {
			return@tx NoCode.LABEL_ADD_ALREADY_EXISTS
		}
		val totalLength = labels.sumOf { it.label.getDisplayLength() } + displayLength
		if (totalLength > MAX_TOTAL_DISPLAY_LENGTH) {
			return@tx NoCode.LABEL_ADD_LENGTH_LIMIT
		}
		val success = UserLabelDao.insertOne(userId, label, color)
		if (success) NoCode.LABEL_ADD_SUCCESS else NoCode.LABEL_ADD_FAILURE
	}
	return ApiResult.create(code)
}

private const val MAX_COUNT = 4
private const val MAX_TOTAL_DISPLAY_LENGTH = 20