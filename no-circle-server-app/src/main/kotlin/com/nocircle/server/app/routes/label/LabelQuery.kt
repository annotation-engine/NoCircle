package com.nocircle.server.app.routes.label

import com.nocircle.server.app.dao.UserLabelDao
import com.nocircle.server.app.plugins.LabelContext
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.noPrincipal
import com.nocircle.server.common.model.respondDTO
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 查询标签
 */
context(_: LabelContext)
fun Route.getQuery() = get("query") {
	val userId = call.noPrincipal!!.userId
	val data = transaction {
		UserLabelDao.getListByUserId(userId).map {
			LabelDTO(it.id.value, it.label, it.color)
		}
	}
	call.respondDTO(data, QueryStatus.SUCCESS)
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