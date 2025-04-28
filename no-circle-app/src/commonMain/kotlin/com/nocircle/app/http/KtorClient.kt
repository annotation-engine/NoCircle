package com.nocircle.app.http

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
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

@Serializable
data class ApiModel<T : Any>(
	val code: Int,
	val msg: String,
	val data: T? = null,
) {
	
	val success by lazy { this.code == 0 }
}