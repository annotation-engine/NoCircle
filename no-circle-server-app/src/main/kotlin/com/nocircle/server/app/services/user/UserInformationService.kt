package com.nocircle.server.app.services.user

import com.nocircle.server.app.plugins.userPrincipal
import com.nocircle.server.common.annotations.Schedule
import com.nocircle.server.common.annotations.ServiceSchedule
import com.nocircle.server.common.models.ApiResult
import com.nocircle.server.common.services.NoParameters
import com.nocircle.server.common.services.NoService
import com.nocircle.server.common.services.noParameters
import io.ktor.http.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@ServiceSchedule(schedule = Schedule.Designing)
object UserInformationService : NoService<UserInformationService.UserInformation> {
	
	override val path = "/user/information"
	
	override val method = HttpMethod.Get
	
	override val auth = true
	
	override suspend fun receive(call: RoutingCall) = noParameters {
		this["userId"] = call.userPrincipal.userId
	}
	
	override suspend fun process(parameters: NoParameters): ApiResult<UserInformation> {
		val userId: Int by parameters
		val data = UserInformation(
			friendCount = 0,
			groupCount = 0,
			messageCount = 0
		)
		return ApiResult.success(data, "用户消息查询成功")
	}
	
	@Serializable
	data class UserInformation(
		val friendCount: Int,
		val groupCount: Int,
		val messageCount: Int
	)
}