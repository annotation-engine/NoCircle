package com.nocircle.server.app.routes.label

import com.nocircle.server.app.tables.user.UserLabels
import com.nocircle.server.common.expends.getDisplayLength
import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.route.NoParameters
import com.nocircle.server.common.route.NoRoute
import com.nocircle.server.common.route.noParameters
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 更新标签
 */
object LabelUpdateRoute : NoRoute<Unit> {
	
	private const val MAX_TOTAL_LENGTH = 20
	
	override val path = "/label/update"
	
	override val method = HttpMethod.Post
	
	override val auth = true
	
	override suspend fun receive(call: RoutingCall) = noParameters(call) {
		val parameters = call.receiveParameters()
		this["id"] = parameters.getInt("id")
		this["label"] = parameters.getString("label")
		this["color"] = parameters.getInt("color")
	}
	
	override suspend fun process(parameters: NoParameters): ApiResult<Unit> {
		val userId = parameters.userId
		val id: Int by parameters
		val label: String by parameters
		val color: Int by parameters
		val code = transaction {
			val displayLength = label.getDisplayLength()
			if (displayLength == 0) {
				return@transaction Code.Empty
			}
			val userLabel = UserLabels.getById(id)
				?: return@transaction Code.NotFound
			
			if (userLabel.label == label && userLabel.color == color) {
				return@transaction Code.NoChange
			}
			
			val labels = UserLabels.getListByUserIdAndNeqId(userId, id)
			if (labels.find { it.label == label } != null) {
				return@transaction Code.AlreadyExists
			}
			val totalLength = labels.sumOf { it.label.getDisplayLength() } + displayLength
			if (totalLength > MAX_TOTAL_LENGTH) {
				return@transaction Code.LengthLimit
			}
			val success = UserLabels.update(userId, id, label, color)
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
		Success("标签修改成功"),
		Failure("标签修改失败"),
		NotFound("未找到标签"),
		NoChange("标签无需修改"),
		AlreadyExists("标签已存在"),
		LengthLimit("超过总长度限制"),
		Empty("标签不能为空")
	}
}