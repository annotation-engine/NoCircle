package com.nocircle.app.ktorfitx

import cn.ktorfitx.multiplatform.core.ktorfit
import cn.ktorfitx.multiplatform.core.model.ApiResult
import com.nocircle.common.config.TokenConfigKey
import com.nocircle.common.config.get
import com.nocircle.common.device.DeviceName.ANDROID
import com.nocircle.common.device.NoDevice
import com.nocircle.shared.serialization.ISOInstantSerializer
import com.nocircle.shared.serialization.contextual
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
val ktorfitx = ktorfit {
	token {
		runBlocking { TokenConfigKey.get() }
	}
	baseUrl = when (NoDevice.Name) {
		ANDROID -> "http://10.0.2.2:8080/api/"
		else -> "http://127.0.0.1:8080/api/"
	}
	httpClient(HttpClientEngineFactory) {
		install(ContentNegotiation) {
			json(
				json = Json {
					serializersModule = SerializersModule {
						contextual<Instant>(ISOInstantSerializer)
					}
					prettyPrint = false
					ignoreUnknownKeys = true
				}
			)
		}
		install(HttpTimeout) {
			requestTimeoutMillis = 10_000L
			socketTimeoutMillis = 10_000L
			connectTimeoutMillis = 10_000L
		}
		install(WebSockets)
	}
}

expect val HttpClientEngineFactory: HttpClientEngineFactory<*>

val <T : Any> ApiResult<T>.success: Boolean
	get() = this.code == 0