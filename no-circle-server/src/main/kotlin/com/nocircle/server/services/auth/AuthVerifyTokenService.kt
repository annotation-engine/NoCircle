package com.nocircle.server.services.auth

import com.nocircle.server.annotations.Schedule
import com.nocircle.server.annotations.ServiceSchedule
import com.nocircle.server.models.ApiResult
import com.nocircle.server.services.NoService
import io.ktor.http.*

@ServiceSchedule(Schedule.Release)
object AuthVerifyTokenService : NoService<Boolean> {
	
	override val path = "/auth/verifyToken"
	
	override val method = HttpMethod.Post
	
	override val auth = true
	
	override suspend fun service(): ApiResult<Boolean> {
		return ApiResult.success("验证成功")
	}
}