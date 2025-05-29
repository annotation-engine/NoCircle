package com.nocircle.server.app.services.label

import com.nocircle.server.app.tables.user.UserLabels
import com.nocircle.server.common.expends.getDisplayLength
import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.services.NoParameters
import com.nocircle.server.common.services.NoService
import com.nocircle.server.common.services.noParameters
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 添加标签
 */
object LabelAddService : NoService<Unit> {
	
	private const val MAX_COUNT = 4
	private const val MAX_TOTAL_LENGTH = 20
	
	override val path = "/label/add"
	
	override val method = HttpMethod.Post
	
	override val auth = true
	
	override suspend fun receive(call: RoutingCall) = noParameters(call) {
		val parameters = call.receiveParameters()
		this["label"] = parameters.getString("label")
		this["color"] = parameters.getInt("color")
	}
	
	override suspend fun process(parameters: NoParameters): ApiResult<Unit> {
		val userId = parameters.userId
		val label: String by parameters
		val color: Int by parameters
		
		val code = transaction {
			val displayLength = label.getDisplayLength()
			if (displayLength == 0) {
				return@transaction Code.Empty
			}
			val labels = UserLabels.getListByUserId(userId)
			if (labels.size >= MAX_COUNT) {
				return@transaction Code.TotalLimit
			}
			if (labels.find { it.label == label } != null) {
				return@transaction Code.AlreadyExists
			}
			val totalLength = labels.sumOf { it.label.getDisplayLength() } + displayLength
			if (totalLength > MAX_TOTAL_LENGTH) {
				return@transaction Code.LengthLimit
			}
			val success = UserLabels.insert(userId, label, color)
			if (success) Code.Success else Code.Failure
		}
		return if (code == Code.Success) {
			ApiResult.success(code.msg)
		} else {
			ApiResult.failure(code.msg)
		}
	}
	
	private enum class Code(
		val msg: String
	) {
		Success("标签添加成功"),
		Failure("标签添加失败"),
		AlreadyExists("标签已存在"),
		TotalLimit("超过最大数量限制"),
		LengthLimit("超过总长度限制"),
		Empty("标签不能为空")
	}
}