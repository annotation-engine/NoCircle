package com.nocircle.server.app.routes.user

import com.nocircle.server.app.plugins.UserContext
import com.nocircle.server.app.tables.user.UserLogins
import com.nocircle.server.app.tables.user.Users
import com.nocircle.server.common.expends.format
import com.nocircle.server.common.model.Status
import com.nocircle.server.common.model.noPrincipal
import com.nocircle.server.common.model.respond
import com.nocircle.server.common.routes.AuthenticateContext
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 用户详情
 */
context(_: UserContext, _: AuthenticateContext)
fun Route.getDetail() = get("detail") {
	val userId = call.noPrincipal!!.userId
	val userDetail = transaction {
		val user = Users.getOneById(userId) ?: return@transaction null
		val lastLoginTime = UserLogins.getLastLoginByUserId(userId)?.loginTime
		UserDetail(
			username = user.username,
			nickname = user.nickname,
			avatarUrl = user.avatarUrl,
			lastLoginTime = lastLoginTime?.format()
		)
	} ?: return@get call.respond(DetailStatus.FAILURE)
	
	call.respond(userDetail, DetailStatus.SUCCESS)
}

@Serializable
private data class UserDetail(
	val username: String,
	val nickname: String?,
	val avatarUrl: String?,
	val lastLoginTime: String?
)

/**
 * 102X
 */
private enum class DetailStatus(
	override val msg: String,
	override val code: Int
) : Status {
	SUCCESS("用户详情查询成功", 0),
	FAILURE("用户详情查询失败", 1020)
}