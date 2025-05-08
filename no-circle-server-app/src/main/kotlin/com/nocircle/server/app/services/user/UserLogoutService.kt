package com.nocircle.server.app.services.user

import com.nocircle.server.app.plugins.RedisPrefix
import com.nocircle.server.app.plugins.redisson
import com.nocircle.server.app.plugins.userPrincipal
import com.nocircle.server.common.annotations.Schedule
import com.nocircle.server.common.annotations.ServiceSchedule
import com.nocircle.server.common.models.ApiResult
import com.nocircle.server.common.services.NoParameters
import com.nocircle.server.common.services.NoService
import com.nocircle.server.common.services.noParameters
import io.ktor.http.*
import io.ktor.server.routing.*

/**
 * 用户登出服务
 */
@ServiceSchedule(schedule = Schedule.Developing)
object UserLogoutService : NoService<Unit> {
	
	override val path = "/user/logout"
	
	override val method = HttpMethod.Post
	
	override val auth = true
	
	override suspend fun receive(call: RoutingCall) = noParameters {
		this["userId"] = call.userPrincipal.userId
	}
	
	override suspend fun process(parameters: NoParameters): ApiResult<Unit> {
		val userId: Int by parameters
		val bucket = redisson.getBucket<String>("${RedisPrefix.USER_TOKEN}$userId")
		bucket.delete()
		return ApiResult.success("登出成功，请重新登录")
	}
}