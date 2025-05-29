package com.nocircle.server.app.services.label

import com.nocircle.server.app.tables.user.UserLabels
import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.services.NoParameters
import com.nocircle.server.common.services.NoService
import com.nocircle.server.common.services.noParameters
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 删除标签
 */
object LabelDeleteService : NoService<Unit> {
	
	override val path = "/label/delete"
	
	override val method = HttpMethod.Post
	
	override val auth = true
	
	override suspend fun receive(call: RoutingCall) = noParameters(call) {
		val parameters = call.receiveParameters()
		this["id"] = parameters.getIntOrFail("id")
	}
	
	override suspend fun process(parameters: NoParameters): ApiResult<Unit> {
		val userId = parameters.userId
		val id: Int by parameters
		val success = transaction {
			UserLabels.delete(userId, id)
		}
		return if (success) {
			ApiResult.success("标签删除成功")
		} else {
			ApiResult.failure("标签删除失败")
		}
	}
}