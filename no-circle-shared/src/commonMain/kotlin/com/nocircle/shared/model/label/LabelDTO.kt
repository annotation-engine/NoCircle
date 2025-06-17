package com.nocircle.shared.model.label

import kotlinx.serialization.Serializable

@Serializable
data class LabelDTO(
	val id: Int,
	val label: String,
	val color: String
)
