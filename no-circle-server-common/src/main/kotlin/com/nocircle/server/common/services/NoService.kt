package com.nocircle.server.common.services

import com.nocircle.server.common.model.ApiResult
import com.nocircle.server.common.model.principal
import io.ktor.http.*
import io.ktor.server.routing.*
import kotlin.reflect.KProperty

interface NoService<out R : Any> {
	
	val path: String
	
	val method: HttpMethod
	
	val auth get() = false
	
	val optional get() = false
	
	val roles get() = arrayOf<String?>(null)
	
	suspend fun receive(call: RoutingCall): NoParameters {
		return if (auth) {
			noParameters(call) {}
		} else NoParameters.None
	}
	
	suspend fun process(parameters: NoParameters): ApiResult<R>
}

class NoParameters {
	
	companion object {
		val None = NoParameters()
	}
	
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
	call: RoutingCall? = null,
	block: NoParameters.() -> Unit
): NoParameters = NoParameters().apply {
	if (call != null) {
		this["userId"] = call.principal.userId
	}
	block()
}