package com.nocircle.server.utils

import com.nocircle.server.annotations.Schedule
import com.nocircle.server.annotations.ServiceSchedule
import com.nocircle.server.services.NoService
import io.ktor.http.*
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
	
	val scheduleTotal: MutableMap<Any, Int>
}

inline operator fun <reified S : NoService<T>, reified T : Any> ServiceScope.plusAssign(service: S) {
	val auth = if (service.auth) {
		" - { auth: true, roles: [${service.roles.filterNotNull().joinToString()}], optional: ${service.optional} }"
	} else ""
	val schedule = S::class.findAnnotation<ServiceSchedule>()?.schedule ?: Schedule.Developing
	scheduleTotal[schedule] = scheduleTotal[schedule]?.let { it + 1 } ?: 1
	NoLog.info("Service: [${service.method}] - ${service.path}$auth${if (schedule != Schedule.Release) " - ![${schedule.name.uppercase()}]!" else ""}")
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
}

private class ServiceScopeImpl(
	override val route: Route
) : ServiceScope {
	
	override val scheduleTotal = mutableMapOf<Any, Int>()
	
	private fun getCount(key: Any): Int {
		return scheduleTotal[key] ?: 0
	}
	
	fun total() {
		NoLog.info("[TOTAL] ${scheduleTotal.values.sum()} [RELEASE] ${getCount(Schedule.Release)} [DEVELOPING] ${getCount(Schedule.Developing)} [DESIGNING] ${getCount(Schedule.Designing)}")
	}
}