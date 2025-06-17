package com.nocircle.server.app.plugins

import com.nocircle.server.common.websockets.keepAliveWebSocket
import io.ktor.server.application.*
import io.ktor.server.websocket.*
import kotlin.time.Duration.Companion.seconds

fun Application.configureWebSockets() {
	install(WebSockets) {
		pingPeriod = 15.seconds
		timeout = 15.seconds
		maxFrameSize = 10 * 1024 * 1024
		masking = false
	}
	keepAliveWebSocket()
}