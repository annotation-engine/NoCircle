package com.nocircle.app.api

import cn.ktorfitx.multiplatform.annotation.*
import com.nocircle.shared.model.ApiResult
import com.nocircle.shared.model.user.UserDetailDTO
import com.nocircle.shared.model.user.UserLoginDTO
import com.nocircle.shared.model.user.UserSearchDTO

@Api(url = "user")
interface UserApi {
	
	@POST("login")
	suspend fun login(
		@Field username: String,
		@Field password: String
	): Result<ApiResult<UserLoginDTO>>
	
	@POST("register")
	suspend fun register(
		@Field username: String,
		@Field password: String,
		@Field nickname: String
	): Result<ApiResult<Unit>>
	
	@BearerAuth
	@POST("logout")
	suspend fun logout(): Result<ApiResult<Unit>>
	
	@BearerAuth
	@GET("detail")
	suspend fun queryDetail(): Result<ApiResult<UserDetailDTO>>
	
	@BearerAuth
	@GET("search")
	suspend fun searchUser(
		@Query username: String
	): Result<ApiResult<UserSearchDTO>>
}