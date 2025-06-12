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
 * 添加标签
 */
object LabelAddRoute : NoRoute<Unit> {
	
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
		
		val status = transaction {
			val displayLength = label.getDisplayLength()
			if (displayLength == 0) {
				return@transaction Status.EMPTY
			}
			val labels = UserLabels.getListByUserId(userId)
			if (labels.size >= MAX_COUNT) {
				return@transaction Status.TOTAL_LIMIT
			}
			if (labels.find { it.label == label } != null) {
				return@transaction Status.ALREADY_EXISTS
			}
			val totalLength = labels.sumOf { it.label.getDisplayLength() } + displayLength
			if (totalLength > MAX_TOTAL_LENGTH) {
				return@transaction Status.LENGTH_LIMIT
			}
			val success = UserLabels.insertOne(userId, label, color)
			if (success) Status.SUCCESS else Status.FAILURE
		}
		return if (status == Status.SUCCESS) {
			ApiResult.success(status.msg)
		} else {
			ApiResult.failure(status.msg)
		}
	}
	
	private enum class Status(
		val msg: String
	) {
		SUCCESS("标签添加成功"),
		FAILURE("标签添加失败"),
		ALREADY_EXISTS("标签已存在"),
		TOTAL_LIMIT("超过最大数量限制"),
		LENGTH_LIMIT("超过总长度限制"),
		EMPTY("标签不能为空")
	}
}