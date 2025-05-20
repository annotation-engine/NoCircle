package com.nocircle.server.app.services.label

import com.nocircle.server.app.tables.user.UserLabels
import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.services.NoParameters
import com.nocircle.server.common.services.NoService
import com.nocircle.server.common.services.noParameters
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.experimental.newSuspendedTransaction

/**
 * 添加标签
 */
object LabelAddService : NoService<Unit> {
	
	override val path = "/label/add"
	
	override val method = HttpMethod.Post
	
	override val auth = true
	
	override suspend fun receive(call: RoutingCall) = noParameters(call) {
		val parameters = call.receiveParameters()
		this["label"] = parameters.getOrFail("label")
		this["color"] = parameters.getOrFail("color")
	}
	
	override suspend fun process(parameters: NoParameters): ApiResult<Unit> {
		val userId: Int by parameters
		val label: String by parameters
		val color: String by parameters
		val success = newSuspendedTransaction {
			val insert = UserLabels.insert {
				it[this.userId] = userId
				it[this.label] = label
				it[this.color] = color
			}
			insert.insertedCount == 1
		}
		return if (success) {
			ApiResult.success("添加标签成功")
		} else {
			ApiResult.failure("添加标签失败")
		}
	}
}