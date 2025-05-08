package com.nocircle.app.http

import com.nocircle.common.device.DeviceName.Android
import com.nocircle.common.device.NoDevice
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

private val NoBaseUrl by lazy {
	when (NoDevice.Name) {
		Android -> "http://10.0.2.2:8080/api/"
		else -> "http://127.0.0.1:8080/api/"
	}
}

val ktorClient by lazy {
	HttpClient(CIO) {
		defaultRequest {
			url(NoBaseUrl)
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