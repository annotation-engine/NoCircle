package com.nocircle.server.app.routes.label

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.POST
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.expends.getPrincipal
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 删除标签
 */
@Authentication
@POST("label/delete")
suspend fun RoutingContext.deleteLabel(): ApiResult<Unit> {
	val userId = call.getPrincipal().userId
	val id: Int by call.receiveParameters()
	val success = transaction {
		UserLabelDao.deleteOne(userId, id)
	}
	val code = if (success) NoCode.LABEL_DELETE_SUCCESS else NoCode.LABEL_DELETE_FAILURE
	return ApiResult.new(code)
}