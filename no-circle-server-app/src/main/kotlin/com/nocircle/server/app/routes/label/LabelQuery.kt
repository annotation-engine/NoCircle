package com.nocircle.server.app.routes.label

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.GET
import cn.ktorfitx.server.annotation.Principal
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.common.expends.create
import com.nocircle.server.common.exposed.tx
import com.nocircle.server.common.model.NoPrincipal
import com.nocircle.shared.model.ApiResult
import com.nocircle.shared.model.label.UserLabelDTO

/**
 * 查询标签
 */
@Authentication
@GET("label/query")
suspend fun queryLabel(
	@Principal principal: NoPrincipal,
): ApiResult<List<UserLabelDTO>> {
	val userId = principal.userId
	val data = tx {
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