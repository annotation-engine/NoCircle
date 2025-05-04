package com.nocircle.server.services

import com.nocircle.server.models.ApiResult
import io.ktor.http.*
import io.ktor.server.routing.*
import kotlin.reflect.KProperty

interface NoService<out R : Any> {
	
	val path: String
	
	val method: HttpMethod
	
	val auth get() = false
	
	val optional get() = false
	
	val roles get() = arrayOf<String?>(null)
	
	suspend fun receive(call: RoutingCall): NoParameters? = null
	
	suspend fun execute(parameters: NoParameters): ApiResult<R> = error("请实现 suspend fun service(parameters: NoParameters): ApiResult<R>")
	
	suspend fun execute(): ApiResult<R> = error("请实现 suspend fun service(): ApiResult<R>")
}

class NoParameters() {
	
	private val parameters = mutableMapOf<String, Any?>()
	
	operator fun <T> set(key: String, value: T) {
		this.parameters[key] = value
	}
	
	@Suppress("UNCHECKED_CAST")
	operator fun <T> get(key: String): T {
		return parameters[key] as T
	}
	
	@Suppress("UNCHECKED_CAST")
	operator fun <T> getValue(thisRef: Any?, property: KProperty<*>): T {
		return this.parameters[property.name] as T
	}
}

inline fun noParameters(
	block: NoParameters.() -> Unit
): NoParameters = NoParameters().apply(block)