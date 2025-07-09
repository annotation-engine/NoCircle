package com.nocircle.server.app.plugins

import com.nocircle.shared.model.ApiResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
	install(StatusPages) {
		val status = HttpStatusCode.allStatusCodes.filter {
			it.value in 300..599
		}.toTypedArray()
		status(*status) {
			val apiResult = apiResultCacheMap.getOrPut(it) {
				ApiResult(it.value, it.description)
			}
			call.respond(apiResult)
		}
	}
}

private val apiResultCacheMap = mutableMapOf<HttpStatusCode, ApiResult<Unit>>()