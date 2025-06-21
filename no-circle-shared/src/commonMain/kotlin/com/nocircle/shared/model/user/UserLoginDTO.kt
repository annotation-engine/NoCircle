package com.nocircle.shared.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UserLoginDTO(
	val userId: Int,
	val token: String
)