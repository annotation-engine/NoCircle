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
)

suspend inline fun <reified T, C : BaseCode> ApplicationCall.respondOK(
	data: T,
	code: C
) = this.respond(HttpStatusCode.OK, ApiResult(code.code, code.msg, data))

suspend inline fun <T : BaseCode> ApplicationCall.respondOK(
	code: T
) = this.respond(HttpStatusCode.OK, ApiResult(code.code, code.msg, null))

interface BaseCode {
	
	val msg: String
	
	val code: Int
}