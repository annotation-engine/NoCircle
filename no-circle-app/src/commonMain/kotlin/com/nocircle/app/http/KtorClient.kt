package com.nocircle.app.http

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

val ktorClient by lazy {
	HttpClient(CIO) {
		defaultRequest {
			url("http://10.0.2.2:8080/api/")
		}
		engine {
			requestTimeout = 10_000L
			maxConnectionsCount = 100
		}
		install(ContentNegotiation) {
			json(Json {
				prettyPrint = true
				ignoreUnknownKeys = true
			})
		}
	}
}