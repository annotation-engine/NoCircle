package com.nocircle.server.app.routes.user

import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.dao.UserLoginDao
import com.nocircle.server.app.plugins.UserRouteGroup
import com.nocircle.server.common.expends.formatToShanghai
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.Authorized
import com.nocircle.server.common.routes.getPrincipal
import com.nocircle.shared.model.user.UserDetailDTO
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 用户详情
 */
context(_: UserRouteGroup, _: Authorized)
fun Route.getUserDetail() = get("detail") {
	val userId = call.getPrincipal().userId
	val userDetail = transaction {
		val user = UserDao.getOneById(userId) ?: return@transaction null
		val lastLoginTime = UserLoginDao.getLastLoginByUserId(userId)?.loginTime
		UserDetailDTO(
			username = user.username,
			nickname = user.nickname,
			avatarUrl = user.avatarUrl,
			lastLoginTime = lastLoginTime?.formatToShanghai()
		)
	} ?: return@get call.respondOK(DetailStatus.FAILURE)
	
	call.respondOK(userDetail, DetailStatus.SUCCESS)
}

/**
 * 102X
 */
private enum class DetailStatus(
	override val msg: String,
	override val code: Int
) : NoStatus {
	SUCCESS("用户详情查询成功", 0),
	FAILURE("用户详情查询失败", 1020)
}