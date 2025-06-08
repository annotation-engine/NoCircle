package com.nocircle.server.app.services.user

import com.nocircle.server.app.plugins.UserToken
import com.nocircle.server.app.plugins.redisson
import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.services.NoParameters
import com.nocircle.server.common.services.NoService
import io.ktor.http.*

/**
 * 用户登出
 */
object UserLogoutService : NoService<Unit> {
	
	override val path = "/user/logout"
	
	override val method = HttpMethod.Post
	
	override val auth = true
	
	override suspend fun process(parameters: NoParameters): ApiResult<Unit> {
		val userId: Int by parameters
		val bucket = redisson.getBucket<String>("${UserToken.prefix}$userId")
		bucket.delete()
		return ApiResult.success("登出成功，请重新登录")
	}
}