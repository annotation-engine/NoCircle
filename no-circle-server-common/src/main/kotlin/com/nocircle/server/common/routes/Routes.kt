package com.nocircle.server.common.routes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

abstract class RouteContext(
	val path: String
) {
	
	open fun Route.routes() {
	
	}
	
	open fun Route.authenticates() {
	
	}
}

fun Application.routeContexts(vararg contexts: RouteContext) {
	routing {
		contexts.forEach {
			with(it) {
				route(it.path) {
					routes()
					authenticate {
						authenticates()
					}
				}
			}
		}
	}
}