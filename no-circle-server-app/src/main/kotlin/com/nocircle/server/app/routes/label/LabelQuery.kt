package com.nocircle.server.app.routes.label

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.GET
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.common.expends.create
import com.nocircle.server.common.expends.getPrincipal
import com.nocircle.shared.model.ApiResult
import com.nocircle.shared.model.label.UserLabelDTO
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 查询标签
 */
@Authentication
@GET("label/query")
fun RoutingContext.queryLabel(): ApiResult<List<UserLabelDTO>> {
	val userId = call.getPrincipal().userId
	val data = transaction {
		UserLabelDao.getListByUserId(userId).map {
			UserLabelDTO(
				id = it.id.value,
				label = it.label,
				color = it.color
			)
		}
	}
	return ApiResult.create(data, NoCode.LABEL_QUERY_SUCCESS)
}