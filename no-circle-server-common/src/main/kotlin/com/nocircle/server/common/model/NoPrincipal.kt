package com.nocircle.server.common.model

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

data class NoPrincipal(
	val userId: Int,
	val username: String,
)

val RoutingCall.noPrincipal: NoPrincipal?
	get() = this.principal<NoPrincipal>()

val ApplicationCall.noPrincipal: NoPrincipal?
	get() = this.principal<NoPrincipal>()