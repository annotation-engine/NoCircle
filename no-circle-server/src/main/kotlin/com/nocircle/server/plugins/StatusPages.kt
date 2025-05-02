package com.nocircle.server.plugins

import com.nocircle.server.models.ApiResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
	install(StatusPages) {
		status(
			HttpStatusCode.BadRequest,
			HttpStatusCode.Unauthorized,
			HttpStatusCode.PaymentRequired,
			HttpStatusCode.Forbidden,
			HttpStatusCode.NotFound,
			HttpStatusCode.MethodNotAllowed,
			HttpStatusCode.InternalServerError
		) {
			call.respond(ApiResult.httpStatus<Nothing>(it))
		}
	}
}