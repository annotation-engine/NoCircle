package com.nocircle.server.app.routes.label

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.Field
import cn.ktorfitx.server.annotation.POST
import cn.ktorfitx.server.annotation.Principal
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.common.expends.create
import com.nocircle.server.common.model.NoPrincipal
import com.nocircle.shared.model.ApiResult
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 删除标签
 */
@Authentication
@POST("label/delete")
fun deleteLabel(
	@Principal principal: NoPrincipal,
	@Field id: Int
): ApiResult<Unit> {
	val userId = principal.userId
	val success = transaction {
		UserLabelDao.deleteOne(userId, id)
	}
	val code = if (success) NoCode.LABEL_DELETE_SUCCESS else NoCode.LABEL_DELETE_FAILURE
	return ApiResult.create(code)
}