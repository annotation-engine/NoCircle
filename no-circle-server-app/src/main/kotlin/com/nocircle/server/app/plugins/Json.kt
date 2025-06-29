package com.nocircle.server.app.plugins

import com.nocircle.shared.serialization.ISOInstantSerializer
import com.nocircle.shared.serialization.contextual
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun Application.configureJson() {
	install(ContentNegotiation) {
		json(
			json = Json {
				serializersModule = SerializersModule {
					contextual<Instant>(ISOInstantSerializer)
				}
				prettyPrint = true
				ignoreUnknownKeys = true
			}
		)
	}
}