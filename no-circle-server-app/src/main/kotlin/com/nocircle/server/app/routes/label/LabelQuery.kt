package com.nocircle.server.app.routes.label

import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.app.plugins.LabelRouteGroup
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.Authorized
import com.nocircle.server.common.routes.getPrincipal
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
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
	call.respondOK(data, QueryStatus.SUCCESS)
}

@Serializable
private data class LabelDTO(
	val id: Int,
	val label: String,
	val color: String
)

/**
 * 112x
 */
private enum class QueryStatus(
	override val msg: String,
	override val code: Int
) : NoStatus {
	SUCCESS("标签查询成功", 0)
}