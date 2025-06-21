package com.nocircle.app.api

import cn.vividcode.multiplatform.ktorfitx.annotation.*
import cn.vividcode.multiplatform.ktorfitx.api.model.ResultBody
import com.nocircle.shared.model.user.UserSearchDTO
import com.nocircle.shared.model.user.UserDetailDTO
import com.nocircle.shared.model.user.UserLoginDTO

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
		@Field nickname: String
	): ResultBody<Unit>?
	
	@BearerAuth
	@POST("logout")
	suspend fun logout(): ResultBody<Unit>?
	
	@BearerAuth
	@GET("detail")
	suspend fun queryDetail(): ResultBody<UserDetailDTO>?
	
	@BearerAuth
	@GET("search")
	suspend fun searchUser(
		@Query username: String
	): ResultBody<UserSearchDTO>?
}