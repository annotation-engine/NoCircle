package com.nocircle.server.services.auth

import com.nocircle.server.annotations.Schedule
import com.nocircle.server.annotations.ServiceSchedule
import com.nocircle.server.models.ApiResult
import com.nocircle.server.services.NoParameters
import com.nocircle.server.services.NoService
import com.nocircle.server.services.noParameters
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

@ServiceSchedule(Schedule.Developing)
object AuthVerifyTokenService : NoService<Boolean> {
	
	override val path = "/auth/verifyToken"
	
	override val method = HttpMethod.Post
	
	override suspend fun receive(call: RoutingCall) = noParameters {
		this["token"] = call.receiveParameters().getOrFail("token")
	}
	
	override suspend fun process(parameters: NoParameters): ApiResult<Boolean> {
		return ApiResult.success("验证成功")
	}
}