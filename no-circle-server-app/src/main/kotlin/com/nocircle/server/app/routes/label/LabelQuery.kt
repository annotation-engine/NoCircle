package com.nocircle.server.app.routes.label

import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.app.plugins.LabelRouteContext
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.AuthContext
import com.nocircle.server.common.routes.getPrincipal
import com.nocircle.shared.model.label.UserLabelDTO
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 查询标签
 */
context(_: LabelRouteContext, _: AuthContext)
fun Route.queryLabel() = get("query") {
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
	call.respondOK(data, NoCode.LABEL_QUERY_SUCCESS)
}