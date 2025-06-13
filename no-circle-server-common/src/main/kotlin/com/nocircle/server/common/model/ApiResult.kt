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

interface Status {
	val msg: String
	val code: Int
}

suspend inline fun <reified T : Any> ApplicationCall.respond(
	data: T,
	status: Status
) = this.respond(HttpStatusCode.OK, ApiResult(status.code, status.msg, data))

suspend inline fun ApplicationCall.respond(
	status: Status
) = this.respond(HttpStatusCode.OK, ApiResult(status.code, status.msg, null))