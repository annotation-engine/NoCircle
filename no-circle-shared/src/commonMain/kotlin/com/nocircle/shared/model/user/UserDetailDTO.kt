package com.nocircle.shared.model.user

import com.nocircle.shared.serialization.ISOInstantSerializer
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class UserDetailDTO(
	val username: String,
	val nickname: String,
	val avatarUrl: String?,
	@Serializable(with = ISOInstantSerializer::class)
	val lastLoginTime: Instant?,
)