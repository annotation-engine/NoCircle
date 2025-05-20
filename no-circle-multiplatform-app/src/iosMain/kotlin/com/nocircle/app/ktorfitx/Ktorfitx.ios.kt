package com.nocircle.app.ktorfitx

import io.ktor.client.engine.*
import io.ktor.client.engine.darwin.*

actual val HttpClientEngineFactory: HttpClientEngineFactory<*> get() = Darwin