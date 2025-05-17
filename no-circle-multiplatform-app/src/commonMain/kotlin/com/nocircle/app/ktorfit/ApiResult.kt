package com.nocircle.app.ktorfit

import kotlinx.serialization.Serializable

@Serializable
data class ApiResult<T : Any>(
	val code: Int,
	val msg: String,
	val data: T? = null
) {
	val success = code == 0
}