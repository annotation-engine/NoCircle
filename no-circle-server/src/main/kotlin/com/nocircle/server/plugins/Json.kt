package com.nocircle.server.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

fun Application.configureJson() {
	install(ContentNegotiation) {
		json(
			json = Json {
				prettyPrint = false
				ignoreUnknownKeys = true
			}
		)
	}
}