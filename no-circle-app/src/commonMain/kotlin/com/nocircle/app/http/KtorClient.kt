package com.nocircle.app.http

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

val ktorClient by lazy {
	HttpClient(CIO) {
		defaultRequest {
			url {
				protocol = URLProtocol.HTTP
				host = "10.0.2.2"
				port = 8080
				encodedPath = "/api"
			}
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

@Serializable
data class ApiModel<T : Any>(
	val code: Int,
	val msg: String,
	val data: T? = null,
) {
	
	val success by lazy { this.code == 0 }
}