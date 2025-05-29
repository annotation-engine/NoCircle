package com.nocircle.server.app.services.label

import com.nocircle.server.app.tables.user.UserLabels
import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.services.NoParameters
import com.nocircle.server.common.services.NoService
import io.ktor.http.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object LabelQueryService : NoService<List<LabelQueryService.Label>> {
	
	override val path = "/label/query"
	
	override val method = HttpMethod.Get
	
	override val auth = true
	
	override suspend fun process(parameters: NoParameters): ApiResult<List<Label>> {
		val userId = parameters.userId
		val data = transaction {
			UserLabels.getListByUserId(userId).map {
				Label(it.id.value, it.label, it.color)
			}
		}
		return ApiResult.success(data, "标签查询成功")
	}
	
	@Serializable
	data class Label(
		val id: Int,
		val label: String,
		val color: Int,
	)
}