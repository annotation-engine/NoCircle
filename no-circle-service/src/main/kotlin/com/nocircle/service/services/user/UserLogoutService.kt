package com.nocircle.service.services.user

import com.nocircle.service.model.ApiResult
import com.nocircle.service.plugins.userPrincipal
import com.nocircle.service.services.KtorService
import com.nocircle.service.utils.Log
import io.ktor.http.*
import io.ktor.server.routing.*

object UserLogoutService : KtorService<Unit> {
	
	override val path = "/user/logout"
	
	override val method = HttpMethod.Post
	
	override val auth = true
	
	context(call: RoutingCall)
	override suspend fun service(): ApiResult<Unit> {
		val userId = call.userPrincipal.userId
		Log.info(userId)
		return ApiResult.failure()
	}
}