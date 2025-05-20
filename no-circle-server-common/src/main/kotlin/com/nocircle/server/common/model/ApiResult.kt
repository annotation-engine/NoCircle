package com.nocircle.server.common.model

import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class ApiResult<out T : Any>(
	val code: Int,
	val msg: String,
	val data: T?
) {
	
	companion object {
		
		fun <T : Any> success(
			data: T,
			msg: String = "操作成功",
		): ApiResult<T> = ApiResult(0, msg, data)
		
		fun <T : Any> success(
			msg: String = "操作成功",
		): ApiResult<T> = ApiResult(0, msg, null)
		
		fun <T : Any> failure(
			msg: String = "操作失败",
			code: Int = -1,
		): ApiResult<T> = ApiResult(code, msg, null)
		
		fun httpStatus(
			status: HttpStatusCode,
		): ApiResult<Unit> = ApiResult(status.value, status.description, null)
	}
}