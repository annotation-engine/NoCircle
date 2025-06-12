package com.nocircle.server.app.routes.label

import com.nocircle.server.app.tables.user.UserLabels
import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.route.NoParameters
import com.nocircle.server.common.route.NoRoute
import com.nocircle.server.common.route.noParameters
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 删除标签
 */
object LabelDeleteRoute : NoRoute<Unit> {
	
	override val path = "/label/delete"
	
	override val method = HttpMethod.Post
	
	override val auth = true
	
	override suspend fun receive(call: RoutingCall) = noParameters(call) {
		val parameters = call.receiveParameters()
		this["id"] = parameters.getInt("id")
	}
	
	override suspend fun process(parameters: NoParameters): ApiResult<Unit> {
		val userId = parameters.userId
		val id: Int by parameters
		val success = transaction {
			UserLabels.deleteOne(userId, id)
		}
		return if (success) {
			ApiResult.success("标签删除成功")
		} else {
			ApiResult.failure("标签删除失败")
		}
	}
}