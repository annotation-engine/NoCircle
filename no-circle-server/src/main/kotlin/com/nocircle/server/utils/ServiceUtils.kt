package com.nocircle.server.utils

import com.nocircle.server.annotations.Schedule
import com.nocircle.server.annotations.ServiceSchedule
import com.nocircle.server.services.NoService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.reflect.full.findAnnotation

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

inline operator fun <reified S : NoService<T>, reified T : Any> ServiceScope.plusAssign(service: S) {
	val auth = if (service.auth) {
		" - { auth: true, roles: [${service.roles.filterNotNull().joinToString()}], optional: ${service.optional} }"
	} else ""
	val schedule = S::class.findAnnotation<ServiceSchedule>()?.schedule ?: Schedule.Developing
	Log.info("Service: [${service.method}] - ${service.path}$auth${if (schedule != Schedule.Release) " - [${schedule.name.uppercase()}]!" else ""}")
	val build: Route.() -> Unit = {
		route(
			path = service.path,
			method = service.method
		) {
			handle {
				with(call) {
					val parameters = service.receiver(this)
					val result = if (parameters == null) service.service() else service.service(parameters)
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