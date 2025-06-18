package com.nocircle.server.app.routes.user

import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.plugins.UserRouteGroup
import com.nocircle.server.app.plugins.UserToken
import com.nocircle.server.app.plugins.redisson
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.Authorized
import com.nocircle.server.common.routes.getPrincipal
import io.ktor.server.routing.*

/**
 * 用户登出
 */
context(_: UserRouteGroup, _: Authorized)
fun Route.postUserLogout() = post("logout") {
	val userId = call.getPrincipal().userId
	val bucket = redisson.getBucket<String>("${UserToken.prefix}$userId")
	bucket.delete()
	call.respondOK(NoCode.USER_LOGOUT_SUCCESS)
}