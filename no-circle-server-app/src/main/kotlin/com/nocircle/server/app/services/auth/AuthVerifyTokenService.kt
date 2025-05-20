package com.nocircle.server.app.services.auth

import com.nocircle.server.common.annotations.Schedule
import com.nocircle.server.common.annotations.ServiceSchedule
import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.services.NoParameters
import com.nocircle.server.common.services.NoService
import io.ktor.http.*

@ServiceSchedule(Schedule.Release)
object AuthVerifyTokenService : NoService<Boolean> {
	
	override val path = "/auth/verifyToken"
	
	override val method = HttpMethod.Post
	
	override val auth = true
	
	override suspend fun process(parameters: NoParameters): ApiResult<Boolean> {
		return ApiResult.success("验证成功")
	}
}