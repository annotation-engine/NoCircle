package com.nocircle.shared.model.user

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class UserDetailDTO(
	val username: String,
	val nickname: String,
	val avatarUrl: String?,
	val email: String?,
	val signature: String?,
	val gender: Gender?,
	@Contextual
	val lastLoginTime: Instant?,
)