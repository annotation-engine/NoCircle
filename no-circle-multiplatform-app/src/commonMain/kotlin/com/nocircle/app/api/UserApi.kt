package com.nocircle.app.api

import androidx.compose.runtime.Immutable
import cn.vividcode.multiplatform.ktorfitx.annotation.*
import cn.vividcode.multiplatform.ktorfitx.api.model.ResultBody
import kotlinx.serialization.Serializable

@Api(url = "user")
interface UserApi {
	
	@POST("login")
	suspend fun login(
		@Field username: String,
		@Field password: String
	): ResultBody<LoginDTO>?
	
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

@Immutable
@Serializable
data class LoginDTO(
	val token: String,
)

@Immutable
@Serializable
data class UserDetailDTO(
	val username: String,
	val nickname: String?,
	val avatarUrl: String?,
	val lastLoginTime: String?,
)