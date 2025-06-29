package com.nocircle.shared.model.user

import kotlinx.serialization.Serializable

@Serializable
enum class Gender {
	MALE,
	FEMALE,
	NON_BINARY,
	TRANSGENDER_MALE,
	TRANSGENDER_FEMALE,
	GENDERQUEER,
	AGENDER,
	BIGENDER,
	OTHER,
	UNDISCLOSED
}