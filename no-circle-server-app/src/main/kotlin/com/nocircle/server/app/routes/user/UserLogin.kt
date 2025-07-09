package com.nocircle.server.app.routes.user

import cn.ktorfitx.server.annotation.POST
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.dao.UserLoginDao
import com.nocircle.server.app.plugins.NoRedisKey
import com.nocircle.server.app.plugins.redisson
import com.nocircle.server.app.plugins.yaml
import com.nocircle.server.app.tables.UserLogins
import com.nocircle.server.app.utils.JWTUtils
import com.nocircle.server.app.utils.PasswordUtils
import com.nocircle.server.common.expends.create
import com.nocircle.shared.model.ApiResult
import com.nocircle.shared.model.user.UserLoginDTO
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.time.toJavaDuration

/**
 * 用户登录
 */
@POST("user/login")
suspend fun RoutingContext.userLogin(): ApiResult<UserLoginDTO> {
	val parameters = call.receiveParameters()
	val username: String by parameters
	val password: String by parameters
	val user = transaction {
		UserDao.getOneByUsername(username)
	}
	if (user == null || !PasswordUtils.verity(password, user.password)) {
		return ApiResult.create(NoCode.USER_LOGIN_USERNAME_OR_PASSWORD_ERROR)
	}
	val userId = user.id.value
	transaction {
		UserLoginDao.insertOne(userId, UserLogins.Method.PASSWORD)
	}
	val token = JWTUtils.generate(userId, user.username)
	val bucket = redisson.getBucket<String>("${NoRedisKey.USER_TOKEN}::${userId}")
	bucket.set(token, yaml.jwt.timeout.toJavaDuration())
	val data = UserLoginDTO(
		userId = userId,
		token = token,
	)
	return ApiResult.create(data, NoCode.USER_LOGIN_SUCCESS)
}