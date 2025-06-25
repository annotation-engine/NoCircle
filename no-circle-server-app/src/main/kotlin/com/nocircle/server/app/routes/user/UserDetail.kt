package com.nocircle.server.app.routes.user

import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.dao.UserLoginDao
import com.nocircle.server.app.plugins.UserRouteContext
import com.nocircle.server.common.expends.toKtInstant
import com.nocircle.server.common.model.respondOK
import com.nocircle.server.common.routes.AuthContext
import com.nocircle.server.common.routes.getPrincipal
import com.nocircle.shared.model.user.UserDetailDTO
import io.ktor.server.routing.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.time.ExperimentalTime

/**
 * 用户详情
 */
@OptIn(ExperimentalTime::class)
context(_: UserRouteContext, _: AuthContext)
fun Route.userDetail() = get("detail") {
	val userId = call.getPrincipal().userId
	val userDetail = transaction {
		val user = UserDao.getOneById(userId) ?: return@transaction null
		val lastLoginTime = UserLoginDao.getLastLoginByUserId(userId)?.loginTime
		UserDetailDTO(
			username = user.username,
			nickname = user.nickname,
			avatarUrl = user.avatarUrl,
			lastLoginTime = lastLoginTime?.toKtInstant()
		)
	} ?: return@get call.respondOK(NoCode.USER_DETAIL_FAILURE)
	
	call.respondOK(userDetail, NoCode.USER_DETAIL_SUCCESS)
}