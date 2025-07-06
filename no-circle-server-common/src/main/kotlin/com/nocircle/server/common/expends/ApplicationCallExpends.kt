package com.nocircle.server.common.expends

import com.nocircle.server.common.model.NoPrincipal
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun ApplicationCall.getPrincipal(): NoPrincipal {
	return this.principal<NoPrincipal>()!!
}

fun ApplicationCall.getPrincipalOrNull(): NoPrincipal? {
	return this.principal<NoPrincipal>()
}