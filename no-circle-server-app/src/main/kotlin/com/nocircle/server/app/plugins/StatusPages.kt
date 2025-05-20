package com.nocircle.server.app.plugins

import com.nocircle.server.common.model.ApiResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
	install(StatusPages) {
		val status = HttpStatusCode.allStatusCodes.filterNot {
			it == HttpStatusCode.OK
		}.toTypedArray()
		status(*status) {
			call.respond(HttpStatusCode.OK, ApiResult.httpStatus(it))
		}
	}
}