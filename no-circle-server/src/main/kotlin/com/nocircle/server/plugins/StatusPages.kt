package com.nocircle.server.plugins

import com.nocircle.server.models.ApiResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
	install(StatusPages) {
		val status = HttpStatusCode.allStatusCodes.filterNot {
			it in HttpStatusCode.OK .. HttpStatusCode.MultiStatus
		}.toTypedArray()
		status(*status) {
			call.respond(HttpStatusCode.OK, ApiResult.httpStatus(it))
		}
	}
}