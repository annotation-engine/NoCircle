package com.nocircle.shared.model.label

import kotlinx.serialization.Serializable

@Serializable
data class UserLabelDTO(
	val id: Int,
	val label: String,
	val color: String
)
