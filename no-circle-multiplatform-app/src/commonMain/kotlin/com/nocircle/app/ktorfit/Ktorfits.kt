package com.nocircle.app.ktorfit

import cn.vividcode.multiplatform.ktorfitx.api.ktorfit
import cn.vividcode.multiplatform.ktorfitx.api.scope.ApiScope
import com.nocircle.common.config.TokenConfigKey
import com.nocircle.common.config.get
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

val ktorfit = ktorfit(NoCircleScope) {
	token {
		runBlocking { TokenConfigKey.get() }
	}
	baseUrl = "http://192.168.1.110:8080/api/"
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

data object NoCircleScope : ApiScope