package com.nocircle.server.common.websockets

import com.nocircle.server.common.log.NoLog
import com.nocircle.server.common.routes.getPrincipalOrNull
import com.nocircle.shared.websocket.WebSocketType
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

private val _sessions = mutableMapOf<Int, DefaultWebSocketServerSession>()
val sessions: Map<Int, DefaultWebSocketServerSession> = _sessions

suspend inline fun sendToReceiver(
	type: WebSocketType,
	senderId: Int,
	receiverId: Int
): Boolean {
	sessions[receiverId]?.send("$type::$senderId") ?: return false
	return true
}

suspend inline fun <reified T> sendToReceiver(
	type: WebSocketType,
	senderId: Int,
	receiverId: Int,
	value: T
): Boolean {
	sessions[receiverId]?.send("$type::$senderId::${Json.encodeToString(value)}") ?: return false
	return true
}

private val CannotAccept = CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Unauthorized")
private val NORMAL = CloseReason(CloseReason.Codes.NORMAL, "Connection closed")

fun Application.keepAliveWebSocket() {
	routing {
		authenticate {
			webSocket("/keepAlive") {
				val principal = call.getPrincipalOrNull() ?: return@webSocket close(CannotAccept)
				if (principal.userId in sessions) {
					sessions[principal.userId]!!.close(NORMAL)
				}
				while (true) {
					if (principal.userId !in sessions) {
						break
					}
					delay(50)
				}
				_sessions[principal.userId] = this
				val username = principal.username
				NoLog.info("[WS] Connect: $username")
				try {
					for (frame in incoming) {
						if (frame is Frame.Close) break
					}
				} catch (e: Exception) {
					NoLog.error(e.message, e.stackTraceToString())
				} finally {
					_sessions -= principal.userId
					NoLog.info("[WS] Disconnect: $username")
				}
			}
		}
	}
}