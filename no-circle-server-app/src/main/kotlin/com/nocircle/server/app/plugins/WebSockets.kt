package com.nocircle.server.app.plugins

import com.nocircle.server.common.websockets.keepAliveWebSocket
import io.ktor.server.application.*
import io.ktor.server.websocket.*
import kotlin.time.Duration.Companion.seconds

fun Application.configureWebSockets() {
	install(WebSockets) {
		pingPeriod = 10.seconds
		timeout = 10.seconds
		maxFrameSize = Long.MAX_VALUE
	}
	keepAliveWebSocket()
}