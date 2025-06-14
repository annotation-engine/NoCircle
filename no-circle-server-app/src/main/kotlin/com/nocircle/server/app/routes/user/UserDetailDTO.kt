package com.nocircle.server.app.routes.user

import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.dao.UserLoginDao
import com.nocircle.server.app.plugins.UserContext
import com.nocircle.server.common.expends.format
import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.noPrincipal
import com.nocircle.server.common.model.respondDTO
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 用户详情
 */
context(_: UserContext)
fun Route.getDetail() = get("detail") {
	val userId = call.noPrincipal!!.userId
	val userDetail = transaction {
		val user = UserDao.getOneById(userId) ?: return@transaction null
		val lastLoginTime = UserLoginDao.getLastLoginByUserId(userId)?.loginTime
		UserDetailDTO(
			username = user.username,
			nickname = user.nickname,
			avatarUrl = user.avatarUrl,
			lastLoginTime = lastLoginTime?.format()
		)
	} ?: return@get call.respondDTO(DetailStatus.FAILURE)
	
	call.respondDTO(userDetail, DetailStatus.SUCCESS)
}

@Serializable
private data class UserDetailDTO(
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
) : NoStatus {
	SUCCESS("用户详情查询成功", 0),
	FAILURE("用户详情查询失败", 1020)
}