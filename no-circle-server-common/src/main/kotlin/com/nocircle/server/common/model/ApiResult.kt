package com.nocircle.server.common.model

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

@Serializable
data class ApiResult<out T : Any>(
	val code: Int,
	val msg: String,
	val data: T?
) {
	
	companion object {
		
		@JvmName("create")
		inline fun <reified T : Any, C : Code> new(data: T, code: C): ApiResult<T> {
			return ApiResult(code.code, code.msg, data)
		}
		
		@JvmName("create")
		inline fun <reified T : Any, C : Code> new(code: C): ApiResult<T> {
			return ApiResult(code.code, code.msg, null)
		}
	}
}

suspend inline fun <reified T, C : Code> ApplicationCall.respondOK(
	data: T,
	code: C
) = this.respond(HttpStatusCode.OK, ApiResult(code.code, code.msg, data))

suspend inline fun <T : Code> ApplicationCall.respondOK(
	code: T
) = this.respond(HttpStatusCode.OK, ApiResult(code.code, code.msg, null))

interface Code {
	
	val msg: String
	
	val code: Int
}