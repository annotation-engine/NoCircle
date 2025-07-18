package com.nocircle.server.app.routes.user

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.POST
import cn.ktorfitx.server.annotation.Principal
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.plugins.NoRedisKey
import com.nocircle.server.app.plugins.redisson
import com.nocircle.server.common.expends.create
import com.nocircle.server.common.model.NoPrincipal
import com.nocircle.shared.model.ApiResult

/**
 * 用户登出
 */
@Authentication
@POST("user/logout")
fun userLogout(
	@Principal principal: NoPrincipal,
): ApiResult<Unit> {
	val userId = principal.userId
	val bucket = redisson.getBucket<String>("${NoRedisKey.USER_TOKEN}::$userId")
	bucket.delete()
	return ApiResult.create(NoCode.USER_LOGOUT_SUCCESS)
}