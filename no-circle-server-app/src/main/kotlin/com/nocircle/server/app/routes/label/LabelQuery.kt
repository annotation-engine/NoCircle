package com.nocircle.server.app.routes.label

import com.nocircle.server.app.plugins.LabelContext
import com.nocircle.server.app.tables.user.UserLabels
import com.nocircle.server.common.model.Status
import com.nocircle.server.common.model.noPrincipal
import com.nocircle.server.common.model.respond
import com.nocircle.server.common.routes.AuthenticateContext
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 查询标签
 */
context(_: LabelContext, _: AuthenticateContext)
fun Route.getQuery() = get("query") {
	val userId = call.noPrincipal!!.userId
	val data = transaction {
		UserLabels.getListByUserId(userId).map {
			Label(it.id.value, it.label, it.color)
		}
	}
	call.respond(data, QueryStatus.SUCCESS)
}

@Serializable
private data class Label(
	val id: Int,
	val label: String,
	val color: Int,
)

/**
 * 202X
 */
private enum class QueryStatus(
	override val msg: String,
	override val code: Int
) : Status {
	SUCCESS("标签查询成功", 0)
}