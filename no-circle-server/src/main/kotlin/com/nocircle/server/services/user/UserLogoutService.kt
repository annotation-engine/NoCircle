package com.nocircle.server.services.user

import com.nocircle.server.annotations.Schedule
import com.nocircle.server.annotations.ServiceSchedule
import com.nocircle.server.models.ApiResult
import com.nocircle.server.plugins.RedisPrefix
import com.nocircle.server.plugins.redisson
import com.nocircle.server.plugins.userPrincipal
import com.nocircle.server.services.NoParameters
import com.nocircle.server.services.NoService
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
	
	override suspend fun receiver(call: RoutingCall): NoParameters {
		val userId = call.userPrincipal.userId
		return NoParameters.create("userId" to userId)
	}
	
	override suspend fun service(parameters: NoParameters): ApiResult<Unit> {
		val userId: Int = parameters["userId"]
		val bucket = redisson.getBucket<String>("${RedisPrefix.USER_TOKEN}$userId")
		bucket.delete()
		return ApiResult.success("登出成功，请重新登录")
	}
}