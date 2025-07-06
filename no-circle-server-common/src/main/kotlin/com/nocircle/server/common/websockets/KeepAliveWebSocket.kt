package com.nocircle.server.common.websockets

import com.nocircle.server.common.log.NoLog
import com.nocircle.server.common.expends.getPrincipalOrNull
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*

private val _sessions = mutableMapOf<Int, MutableList<DefaultWebSocketServerSession>>()
val sessions: Map<Int, MutableList<DefaultWebSocketServerSession>> = _sessions

private val CannotAccept = CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Unauthorized")

fun Application.keepAliveWebSocket() {
	routing {
		authenticate {
			webSocket("/keepAlive") {
				val principal = call.getPrincipalOrNull() ?: return@webSocket close(CannotAccept)
				val sessions = _sessions.getOrPut(principal.userId) { mutableListOf() }
				sessions += this
				val username = principal.username
				NoLog.info("[WS] Cconnect: $username")
				try {
					for (frame in incoming) {
						if (frame is Frame.Close) break
					}
				} catch (e: Exception) {
					NoLog.error(e.message, e.stackTraceToString())
				} finally {
					sessions -= this
					NoLog.info("[WS] Disconnect: $username")
				}
			}
		}
	}
}