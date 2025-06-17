package com.nocircle.server.common.routes

import com.nocircle.server.common.log.NoLog
import com.nocircle.server.common.model.NoPrincipal
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import kotlin.time.measureTimedValue

abstract class NoRouteGroup(
	val path: String
) {
	
	open fun Route.routes() {
	
	}
	
	context(_: Authorized)
	open fun Route.authenticates() {
	
	}
}

interface Authorized

private object AuthorizedImpl : Authorized

context(_: Authorized)
fun ApplicationCall.getPrincipal(): NoPrincipal {
	return this.principal<NoPrincipal>()!!
}

fun ApplicationCall.getPrincipalOrNull(): NoPrincipal? {
	return this.principal<NoPrincipal>()
}

fun Application.routeContexts(vararg contexts: NoRouteGroup) {
	val timedValue = measureTimedValue {
		routing {
			contexts.forEach {
				with(it) {
					route(it.path) {
						routes()
						authenticate {
							context(AuthorizedImpl) {
								authenticates()
							}
						}
					}
				}
			}
		}
		val infos = routingRoot.getRouteInfos()
		val maxLength = infos.values.maxOf { it.length }
		infos.forEach { (path, method) ->
			NoLog.info("[$method] ${"-".repeat(maxLength - method.length + 2)} $path")
		}
		infos
	}
	NoLog.info("[TOTAL] ${timedValue.value.size}, load all routes used for ${timedValue.duration.inWholeMilliseconds / 1000f} seconds.")
}

fun RoutingNode.getRouteInfos(): Map<String, String> {
	val infos = mutableMapOf<String, String>()
	if (children.isEmpty()) {
		val method = (this.selector as? HttpMethodRouteSelector)?.method
		if (method != null) {
			infos[path] = method.toString()
		}
		return infos
	}
	children.forEach {
		infos += it.getRouteInfos()
	}
	return infos
}