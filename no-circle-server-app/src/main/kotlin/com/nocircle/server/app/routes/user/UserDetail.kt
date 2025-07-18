package com.nocircle.server.app.routes.user

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.GET
import cn.ktorfitx.server.annotation.Principal
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.dao.UserDetailDao
import com.nocircle.server.app.dao.UserLoginDao
import com.nocircle.server.common.expends.create
import com.nocircle.server.common.expends.toKtInstant
import com.nocircle.server.common.model.NoPrincipal
import com.nocircle.shared.model.ApiResult
import com.nocircle.shared.model.user.UserDetailDTO
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.time.ExperimentalTime

/**
 * 用户详情
 */
@OptIn(ExperimentalTime::class)
@Authentication
@GET("user/detail")
fun userDetail(
	@Principal principal: NoPrincipal,
): ApiResult<UserDetailDTO> {
	val userId = principal.userId
	val data = transaction {
		val user = UserDao.getOneById(userId) ?: return@transaction null
		val userDetail = UserDetailDao.getOneByUserId(userId)
		val lastLoginTime = UserLoginDao.getLastLoginByUserId(userId)?.loginTime
		UserDetailDTO(
			username = user.username,
			nickname = user.nickname,
			avatarUrl = userDetail.avatarUrl,
			email = userDetail.email,
			signature = userDetail.signature,
			gender = userDetail.gender,
			lastLoginTime = lastLoginTime?.toKtInstant()
		)
	}
	return if (data != null) {
		ApiResult.create(data, NoCode.USER_DETAIL_SUCCESS)
	} else {
		ApiResult.create(NoCode.USER_DETAIL_FAILURE)
	}
}