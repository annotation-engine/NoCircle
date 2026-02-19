package com.nocircle.server.app.routes.user

import cn.ktorfitx.server.annotation.Authentication
import cn.ktorfitx.server.annotation.GET
import cn.ktorfitx.server.annotation.Principal
import com.nocircle.server.app.code.NoCode
import com.nocircle.server.app.dao.UserDao
import com.nocircle.server.app.dao.UserDetailDao
import com.nocircle.server.app.dao.UserLoginDao
import com.nocircle.server.common.expends.create
import com.nocircle.server.common.exposed.tx
import com.nocircle.server.common.model.NoPrincipal
import com.nocircle.shared.model.ApiResult
import com.nocircle.shared.model.user.UserDetailDTO

/**
 * 用户详情
 */
@Authentication
@GET("user/detail")
suspend fun getUserDetail(
	@Principal principal: NoPrincipal,
): ApiResult<UserDetailDTO> {
	val userId = principal.userId
	val data = tx {
		val user = UserDao.getOneById(userId) ?: return@tx null
		val userDetail = UserDetailDao.getOneByUserId(userId)
		val lastLoginTime = UserLoginDao.getLastLoginByUserId(userId)?.loginTime
		UserDetailDTO(
			username = user.username,
			nickname = user.nickname,
			avatarUrl = userDetail.avatarUrl,
			email = userDetail.email,
			signature = userDetail.signature,
			gender = userDetail.gender,
			lastLoginTime = lastLoginTime
		)
	}
	return if (data != null) {
		ApiResult.create(data, NoCode.USER_DETAIL_SUCCESS)
	} else {
		ApiResult.create(NoCode.USER_DETAIL_FAILURE)
	}
}