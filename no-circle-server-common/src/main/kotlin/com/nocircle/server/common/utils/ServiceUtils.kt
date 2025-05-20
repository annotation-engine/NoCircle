package com.nocircle.server.common.utils

import com.nocircle.server.common.services.NoService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.system.measureTimeMillis

fun Application.services(scope: ServiceScope.() -> Unit) {
	val configuration: Routing.() -> Unit = {
		val serviceScope: ServiceScopeImpl
		val millis = measureTimeMillis {
			serviceScope = ServiceScopeImpl(this).apply(scope)
		}
		serviceScope.total(millis)
	}
	pluginOrNull(RoutingRoot)?.apply(configuration) ?: install(RoutingRoot, configuration)
}

sealed interface ServiceScope {
	
	val route: Route
	
	val services: MutableList<NoService<*>>
}

inline operator fun <reified S : NoService<T>, reified T : Any> ServiceScope.plusAssign(service: S) {
	val build: Route.() -> Unit = {
		route(
			path = service.path,
			method = service.method
		) {
			handle {
				val parameters = service.receive(call)
				val result = service.process(parameters)
				call.respond(HttpStatusCode.OK, result)
			}
		}
	}
	if (service.auth) {
		route.authenticate(
			configurations = service.roles,
			optional = service.optional,
			build = build
		)
	} else {
		route.build()
	}
	this.services += service
}

private class ServiceScopeImpl(
	override val route: Route
) : ServiceScope {
	
	override val services = mutableListOf<NoService<*>>()
	
	fun total(millis: Long) {
		val totalMillis = measureTimeMillis {
			val maxMethodLength = services.maxOf { it.method.toString().length }
			services.forEach {
				val split = "-".repeat(maxMethodLength - it.method.toString().length + 1)
				NoLog.info("[${it.method}] $split ${it.path}${if (it.auth) " *" else ""}")
			}
		}
		NoLog.info("[TOTAL] ${services.size} used for ${(millis + totalMillis) / 1000f} seconds")
	}
}