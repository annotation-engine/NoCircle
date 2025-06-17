package com.nocircle.server.app.plugins

import com.nocircle.server.common.model.NoStatus
import com.nocircle.server.common.model.respondOK
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*

fun Application.configureStatusPages() {
	install(StatusPages) {
		val status = HttpStatusCode.allStatusCodes.filter {
			it.value in 300..599
		}.toTypedArray()
		status(*status) {
			call.respondOK(it.toStatus())
		}
	}
}

private class HttpStatus(
	override val msg: String,
	override val code: Int
) : NoStatus

private val httpStatusCodeMap = mutableMapOf<HttpStatusCode, HttpStatus>()

private fun HttpStatusCode.toStatus(): NoStatus {
	return httpStatusCodeMap.getOrPut(this) {
		HttpStatus(description, value)
	}
}