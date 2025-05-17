package com.nocircle.app.ktorfitx

import cn.vividcode.multiplatform.ktorfitx.api.ktorfit
import cn.vividcode.multiplatform.ktorfitx.api.model.ResultBody
import com.nocircle.common.config.TokenConfigKey
import com.nocircle.common.config.get
import com.nocircle.common.device.DeviceName.Android
import com.nocircle.common.device.NoDevice
import io.ktor.client.engine.cio.*
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
	httpClient(CIO) {
		engine {
			requestTimeout = 10_000L
			maxConnectionsCount = 200
		}
		install(ContentNegotiation) {
			json(Json {
				prettyPrint = false
				ignoreUnknownKeys = true
			})
		}
	}
}

val <T : Any> ResultBody<T>.success: Boolean get() = this.code == 0