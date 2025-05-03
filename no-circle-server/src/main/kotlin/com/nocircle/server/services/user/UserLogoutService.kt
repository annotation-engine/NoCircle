package com.nocircle.server.services.user

import com.nocircle.server.models.ApiResult
import com.nocircle.server.plugins.RedisPrefix
import com.nocircle.server.plugins.redisson
import com.nocircle.server.plugins.userPrincipal
import com.nocircle.server.services.KtorService
import io.ktor.http.*
import io.ktor.server.routing.*

/**
 * 用户登出服务
 */
object UserLogoutService : KtorService<Unit> {
	
	override val path = "/user/logout"
	
	override val method = HttpMethod.Post
	
	override val auth = true
	
	context(call: RoutingCall)
	override suspend fun service(): ApiResult<Unit> {
		val userId = call.userPrincipal.userId
		val bucket = redisson.getBucket<String>("${RedisPrefix.USER_TOKEN}$userId")
		bucket.delete()
		return ApiResult.success("登出成功，请重新登录")
	}
}