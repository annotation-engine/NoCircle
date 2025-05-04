package com.nocircle.app.http

import com.nocircle.app.utils.ConfigUtils
import com.nocircle.common.expends.CurrentDevice
import com.nocircle.common.expends.Device
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.bearerAuth
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

val ktorClient by lazy {
	HttpClient(CIO) {
		defaultRequest {
			url(
				when (CurrentDevice) {
					Device.Android -> "http://10.0.2.2:8080/api/"
					Device.IOS -> "http://127.0.0.1:8080/api/"
					Device.Desktop -> "http://127.0.0.1:8080/api/"
				}
			)
			runBlocking {
				ConfigUtils.getValue<String>("token")?.let {
					bearerAuth(it)
				}
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