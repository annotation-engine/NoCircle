package com.nocircle.server.app.routes.user

import com.nocircle.server.app.plugins.UserContext
import com.nocircle.server.app.plugins.UserToken
import com.nocircle.server.app.plugins.redisson
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.noPrincipal
import com.nocircle.server.common.model.respondDTO
import io.ktor.server.routing.*

/**
 * 用户登出
 */
context(_: UserContext)
fun Route.postUserLogout() = post("logout") {
	val userId = call.noPrincipal!!.userId
	val bucket = redisson.getBucket<String>("${UserToken.prefix}$userId")
	bucket.delete()
	call.respondDTO(LogoutStatus.SUCCESS)
}

/**
 * 101x
 */
private enum class LogoutStatus(
	override val msg: String,
	override val code: Int
) : NoStatus {
	SUCCESS("登出成功，请重新登录", 0)
}