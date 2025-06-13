package com.nocircle.server.app.plugins

import com.nocircle.server.common.model.Status
import com.nocircle.server.common.model.respond
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*

fun Application.configureStatusPages() {
	install(StatusPages) {
		val status = HttpStatusCode.allStatusCodes.filter {
			it.value in 300..599
		}.toTypedArray()
		status(*status) {
			call.respond(it.toStatus())
		}
	}
}

private class HttpStatus(
	override val msg: String,
	override val code: Int
) : Status

private val httpStatusCodeMap = mutableMapOf<HttpStatusCode, HttpStatus>()

private fun HttpStatusCode.toStatus(): Status {
	return httpStatusCodeMap.getOrPut(this) {
		HttpStatus(description, value)
	}
}