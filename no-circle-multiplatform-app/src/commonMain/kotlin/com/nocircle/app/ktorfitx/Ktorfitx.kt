package com.nocircle.app.ktorfitx

import cn.vividcode.multiplatform.ktorfitx.api.ktorfit
import cn.vividcode.multiplatform.ktorfitx.api.model.ResultBody
import com.nocircle.common.config.TokenConfigKey
import com.nocircle.common.config.get
import com.nocircle.common.device.DeviceName.Android
import com.nocircle.common.device.NoDevice
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

val ktorfitx = ktorfit {
	token {
		runBlocking { TokenConfigKey.get() }
	}
	baseUrl = when (NoDevice.Name) {
		Android -> "http://10.0.2.2:8080/api/"
		else -> "http://127.0.0.1:8080/api/"
	}
	httpClient(HttpClientEngineFactory) {
		install(ContentNegotiation) {
			json(Json {
				prettyPrint = false
				ignoreUnknownKeys = true
			})
		}
		install(HttpTimeout) {
			requestTimeoutMillis = 10_000L
			socketTimeoutMillis = 10_000L
			connectTimeoutMillis = 10_000L
		}
	}
}

expect val HttpClientEngineFactory: HttpClientEngineFactory<*>

val <T : Any> ResultBody<T>.success: Boolean
	get() = this.code == 0