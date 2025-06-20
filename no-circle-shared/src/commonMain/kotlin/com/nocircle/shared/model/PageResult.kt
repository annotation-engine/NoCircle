package com.nocircle.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class PageResult<out T : Any>(
	val items: List<T>,
	val total: Int,
)