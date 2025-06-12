package com.nocircle.server.common.route

import com.nocircle.server.common.log.NoLog
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.system.measureTimeMillis

fun Application.routes(
	scope: RouteScope.() -> Unit
) {
	routing {
		val serviceScope: RouteScopeImpl
		val millis = measureTimeMillis {
			serviceScope = RouteScopeImpl(this).apply(scope)
		}
		serviceScope.total(millis)
	}
}

sealed interface RouteScope {
	
	val route: Route
	
	val routeList: MutableList<NoRoute<*>>
}

inline operator fun <reified S : NoRoute<T>, reified T : Any> RouteScope.plusAssign(service: S) {
	route.authenticateOrDefault(service) {
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
	this.routeList += service
}

fun Route.authenticateOrDefault(service: NoRoute<*>, build: Route.() -> Unit) {
	if (service.auth) {
		this.authenticate(
			configurations = service.roles,
			optional = service.optional,
			build = build
		)
	} else {
		this.build()
	}
}

private class RouteScopeImpl(
	override val route: Route
) : RouteScope {
	
	override val routeList = mutableListOf<NoRoute<*>>()
	
	fun total(millis: Long) {
		val totalMillis = measureTimeMillis {
			val maxMethodLength = routeList.maxOf { it.method.toString().length }
			routeList.forEach {
				val split = "-".repeat(maxMethodLength - it.method.toString().length + 1)
				NoLog.info("[${it.method}] $split ${it.path}${if (it.auth) " *" else ""}")
			}
		}
		NoLog.info("[TOTAL] ${routeList.size} used for ${(millis + totalMillis) / 1000f} seconds")
	}
}

inline fun RouteScope.group(scope: RouteScope.() -> Unit) {
	scope()
}