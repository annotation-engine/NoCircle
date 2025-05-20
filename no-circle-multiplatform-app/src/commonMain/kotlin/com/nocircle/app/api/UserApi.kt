package com.nocircle.app.api

import cn.vividcode.multiplatform.ktorfitx.annotation.*
import cn.vividcode.multiplatform.ktorfitx.api.model.ResultBody
import kotlinx.serialization.Serializable

@Api(url = "user")
interface UserApi {
	
	@POST("login")
	suspend fun login(
		@Form username: String,
		@Form password: String
	): ResultBody<LoginVO>?
	
	@POST("register")
	suspend fun register(
		@Form username: String,
		@Form password: String,
	): ResultBody<Unit>?
	
	@BearerAuth
	@POST("logout")
	suspend fun logout(): ResultBody<Unit>?
	
	@BearerAuth
	@GET("detail")
	suspend fun queryDetail(): ResultBody<UserDetailVO>?
}

@Serializable
data class LoginVO(
	val token: String,
)

@Serializable
data class UserDetailVO(
	val username: String,
	val nickname: String?,
	val avatarUrl: String?
)