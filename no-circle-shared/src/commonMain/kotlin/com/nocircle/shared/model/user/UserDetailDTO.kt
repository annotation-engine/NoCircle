package com.nocircle.shared.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UserDetailDTO(
	val username: String,
	val nickname: String,
	val avatarUrl: String?,
	val lastLoginTime: String?,
)