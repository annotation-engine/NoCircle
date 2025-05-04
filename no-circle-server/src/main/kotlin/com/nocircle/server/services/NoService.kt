package com.nocircle.server.services

import com.nocircle.server.models.ApiResult
import io.ktor.http.*
import io.ktor.server.routing.*

interface NoService<out R : Any> {
	
	val path: String
	
	val method: HttpMethod
	
	val auth get() = false
	
	val optional get() = false
	
	val roles get() = arrayOf<String?>(null)
	
	suspend fun receiver(call: RoutingCall): NoParameters? = null
	
	suspend fun service(parameters: NoParameters): ApiResult<R> = error("请实现 suspend fun service(parameters: NoParameters): ApiResult<R>")
	
	suspend fun service(): ApiResult<R> = error("请实现 suspend fun service(): ApiResult<R>")
}

class NoParameters private constructor(
	private val parameters: Map<String, Any?>,
) {
	
	companion object {
		
		fun create(vararg parameters: Pair<String, Any?>): NoParameters {
			return NoParameters(parameters.toMap())
		}
	}
	
	@Suppress("UNCHECKED_CAST")
	operator fun <T> get(key: String): T {
		return parameters[key] as T
	}
}