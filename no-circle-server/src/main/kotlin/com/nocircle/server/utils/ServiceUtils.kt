package com.nocircle.server.utils

import com.nocircle.server.services.KtorService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.services(scope: ServiceScope.() -> Unit) {
	val configuration: Routing.() -> Unit = {
		ServiceScopeImpl(this).apply(scope).total()
	}
	pluginOrNull(RoutingRoot)?.apply(configuration) ?: install(RoutingRoot, configuration)
}

sealed interface ServiceScope {
	
	val route: Route
	
	var serviceCount: Int
	
	var authServiceCount: Int
}

inline operator fun <reified S : KtorService<T>, reified T : Any> ServiceScope.plusAssign(service: S) {
	val auth = if (service.auth) {
		" - { auth: true, roles: [${service.roles.filterNotNull().joinToString()}], optional: ${service.optional} }"
	} else ""
	Log.info("Service: [${service.method}] - ${service.path}$auth")
	val build: Route.() -> Unit = {
		route(
			path = service.path,
			method = service.method
		) {
			handle {
				with(call) {
					val result = service.service()
					call.respond(result)
				}
			}
		}
	}
	if (service.auth) {
		this.authServiceCount++
		route.authenticate(
			configurations = service.roles,
			optional = service.optional
		) {
			route.build()
		}
	} else {
		this.serviceCount++
		route.build()
	}
}

private class ServiceScopeImpl(
	override val route: Route
) : ServiceScope {
	
	override var serviceCount = 0
	override var authServiceCount = 0
	
	fun total() {
		Log.info("Total: $serviceCount services, $authServiceCount auth services, ${serviceCount + authServiceCount} totals.")
	}
}