package com.nocircle.server.app.routes.user

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.POST
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.plugins.NoRedisKey
import com.nocircle.server.app.plugins.redisson
import com.nocircle.server.common.expends.create
import com.nocircle.server.common.expends.getPrincipal
import com.nocircle.shared.model.ApiResult
import io.ktor.server.routing.*

/**
 * 用户登出
 */
@Authentication
@POST("user/logout")
fun RoutingContext.userLogout(): ApiResult<Unit> {
	val userId = call.getPrincipal().userId
	val bucket = redisson.getBucket<String>("${NoRedisKey.USER_TOKEN}::$userId")
	bucket.delete()
	return ApiResult.create(NoCode.USER_LOGOUT_SUCCESS)
}