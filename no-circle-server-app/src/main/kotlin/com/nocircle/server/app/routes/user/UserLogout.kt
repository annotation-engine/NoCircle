package com.nocircle.server.app.routes.user

import com.nocircle.server.app.plugins.UserContext
import com.nocircle.server.app.plugins.UserToken
import com.nocircle.server.app.plugins.redisson
import com.nocircle.server.common.model.Status
import com.nocircle.server.common.model.noPrincipal
import com.nocircle.server.common.model.respond
import com.nocircle.server.common.routes.AuthenticateContext
import io.ktor.server.routing.*

/**
 * 用户登出
 */
context(_: UserContext, _: AuthenticateContext)
fun Route.postLogout() = post("logout") {
	val userId = call.noPrincipal!!.userId
	val bucket = redisson.getBucket<String>("${UserToken.prefix}$userId")
	bucket.delete()
	call.respond(LogoutStatus.SUCCESS)
}

/**
 * 101X
 */
private enum class LogoutStatus(
	override val msg: String,
	override val code: Int
) : Status {
	SUCCESS("登出成功，请重新登录", 0)
}