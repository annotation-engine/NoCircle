package com.nocircle.server.app.routes.user

import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.plugins.NoRedisKey
import com.nocircle.server.app.plugins.UserRouteContext
import com.nocircle.server.app.plugins.redisson
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.AuthContext
import com.nocircle.server.common.routes.getPrincipal
import io.ktor.server.routing.*

/**
 * 用户登出
 */
context(_: UserRouteContext, _: AuthContext)
fun Route.userLogout() = post("logout") {
	val userId = call.getPrincipal().userId
	val bucket = redisson.getBucket<String>("${NoRedisKey.USER_TOKEN}::$userId")
	bucket.delete()
	call.respondOK(NoCode.USER_LOGOUT_SUCCESS)
}