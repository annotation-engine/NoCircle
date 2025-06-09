package com.nocircle.server.app.plugins

import com.nocircle.server.common.websockets.webSocketDispatch
import io.ktor.server.application.*

fun Application.configureWebSockets() {
	webSocketDispatch {
	
	}
}