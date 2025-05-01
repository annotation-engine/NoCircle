package com.nocircle.server.services

import com.nocircle.server.model.ApiResult
import io.ktor.http.*
import io.ktor.server.routing.*

interface KtorService<T : Any> {
	
	val path: String
	
	val method: HttpMethod
	
	val auth get() = false
	
	val optional get() = false
	
	val roles get() = arrayOf<String?>(null)
	
	context(call: RoutingCall)
	suspend fun service(): ApiResult<T>
}