package com.nocircle.server.app.routes.user

import cn.ktorfitx.server.annotation.Field
import cn.ktorfitx.server.annotation.POST
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.dao.UserDetailDao
import com.nocircle.server.common.expends.create
import com.nocircle.server.common.expends.isLowerCases
import com.nocircle.shared.model.ApiResult
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * 用户注册
 */
@POST("user/register")
fun userRegister(
	@Field username: String,
	@Field password: String,
	@Field nickname: String,
): ApiResult<Unit> {
	if (username.length !in 8..12 ||
		!username.isLowerCases() ||
		password.length !in 8..20 ||
		nickname.isBlank() ||
		nickname.length > 20
	) {
		return ApiResult.create(NoCode.USER_REGISTER_FAILURE)
	}
	val code = transaction {
		val exists = UserDao.isExistsByUsername(username)
		if (exists) {
			return@transaction NoCode.USER_REGISTER_USER_ALREADY_EXISTS
		}
		val userId = UserDao.insertOne(username, password, nickname)
		if (userId == null) {
			return@transaction NoCode.USER_REGISTER_FAILURE
		}
		val success = UserDetailDao.insertOne(userId)
		if (success) NoCode.USER_REGISTER_SUCCESS else NoCode.USER_REGISTER_FAILURE
	}
	return ApiResult.create(code)
}