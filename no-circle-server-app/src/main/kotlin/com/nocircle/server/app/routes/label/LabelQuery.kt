package com.nocircle.server.app.routes.label

import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.app.plugins.LabelRouteGroup
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.Authorized
import com.nocircle.server.common.routes.getPrincipal
import com.nocircle.server.app.code.NoCode
import com.nocircle.shared.model.label.LabelDTO
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 查询标签
 */
context(_: LabelRouteGroup, _: Authorized)
fun Route.getQueryLabel() = get("query") {
	val userId = call.getPrincipal().userId
	val data = transaction {
		UserLabelDao.getListByUserId(userId).map {
			LabelDTO(it.id.value, it.label, it.color)
		}
	}
	call.respondOK(data, NoCode.LABEL_QUERY_SUCCESS)
}