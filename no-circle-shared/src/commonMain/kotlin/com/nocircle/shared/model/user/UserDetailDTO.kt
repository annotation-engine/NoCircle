package com.nocircle.shared.model.user

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class UserDetailDTO(
	val username: String,
	val nickname: String,
	val avatarUrl: String?,
	val email: String?,
	val signature: String?,
	val gender: Gender?,
	val lastLoginTime: Instant?,
)