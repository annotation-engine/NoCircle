package com.nocircle.server.services.user

import com.nocircle.server.models.ApiResult
import com.nocircle.server.plugins.userPrincipal
import com.nocircle.server.services.KtorService
import com.nocircle.server.utils.Log
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