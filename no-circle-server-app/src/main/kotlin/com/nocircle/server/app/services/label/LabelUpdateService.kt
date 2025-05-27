package com.nocircle.server.app.services.label

import com.nocircle.server.common.expends.getDisplayLength
import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.services.NoParameters
import com.nocircle.server.common.services.NoService
import com.nocircle.server.common.services.noParameters
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

/**
 * 添加标签
 */
object LabelUpdateService : NoService<Unit> {
	
	override val path = "/label/update"
	
	override val method = HttpMethod.Post
	
	override val auth = true
	
	override suspend fun receive(call: RoutingCall) = noParameters(call) {
		val parameters = call.receiveParameters()
		this["id"] = parameters.getIntOrFail("id")
		this["label"] = parameters.getOrFail("label")
		this["color"] = parameters.getOrFail("color")
	}
	
	override suspend fun process(parameters: NoParameters): ApiResult<Unit> {
		val userId: Int by parameters
		val id: Int by parameters
		val label: String by parameters
		val color: String by parameters
		if (label.getDisplayLength() !in 1 .. 10) {
			return ApiResult.failure("修改标签失败")
		}
		TODO()
	}
}