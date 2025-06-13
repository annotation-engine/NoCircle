package com.nocircle.server.common.routes

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import kotlin.reflect.full.createInstance

interface RouteContext {
	val path: String
}

sealed interface AuthenticateContext

private object AuthenticateContextImpl : AuthenticateContext

val AuthenticateContextInstance: AuthenticateContext = AuthenticateContextImpl

inline fun <reified C : RouteContext> Route.authenticateRoute(
	crossinline build: context(C, AuthenticateContext) Route.() -> Unit
) {
	val context = C::class.objectInstance ?: C::class.createInstance()
	authenticate {
		route(context.path) {
			context(context, AuthenticateContextInstance) {
				build()
			}
		}
	}
}

inline fun <reified C : RouteContext> Route.route(
	crossinline build: context(C) Route.() -> Unit
) {
	val context = C::class.objectInstance ?: C::class.createInstance()
	route(context.path) {
		context(context) {
			build()
		}
	}
}