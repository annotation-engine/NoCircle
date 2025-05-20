package com.nocircle.app.ktorfitx

import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*

actual val HttpClientEngineFactory: HttpClientEngineFactory<*>
	get() = OkHttp