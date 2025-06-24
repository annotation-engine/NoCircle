package com.nocircle.server.app.routes.label

import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.app.plugins.LabelRouteContext
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.AuthContext
import com.nocircle.server.common.routes.getPrincipal
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 删除标签
 */
context(_: LabelRouteContext, _: AuthContext)
fun Route.deleteLabel() = post("delete") {
	val userId = call.getPrincipal().userId
	val id: Int by call.receiveParameters()
	val success = transaction {
		UserLabelDao.deleteOne(userId, id)
	}
	val code = if (success) NoCode.LABEL_DELETE_SUCCESS else NoCode.LABEL_DELETE_FAILURE
	call.respondOK(code)
}