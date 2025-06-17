package com.nocircle.app.api

import cn.vividcode.multiplatform.ktorfitx.annotation.*
import cn.vividcode.multiplatform.ktorfitx.api.model.ResultBody
import com.nocircle.shared.model.user.UserLoginDTO
import com.nocircle.shared.model.user.UserDetailDTO

@Api(url = "user")
interface UserApi {
	
	@POST("login")
	suspend fun login(
		@Field username: String,
		@Field password: String
	): ResultBody<UserLoginDTO>?
	
	@POST("register")
	suspend fun register(
		@Field username: String,
		@Field password: String,
	): ResultBody<Unit>?
	
	@BearerAuth
	@POST("logout")
	suspend fun logout(): ResultBody<Unit>?
	
	@BearerAuth
	@GET("detail")
	suspend fun queryDetail(): ResultBody<UserDetailDTO>?
}