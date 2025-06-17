package com.nocircle.server.common.websockets

import com.nocircle.server.common.expends.associateWithNotNull
import com.nocircle.server.common.log.NoLog
import com.nocircle.server.common.routes.getPrincipalOrNull
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json

private val sessions = mutableMapOf<Int, DefaultWebSocketServerSession>()

fun getSession(userId: Int): DefaultWebSocketServerSession? {
	return sessions[userId]
}

fun getSessions(userIds: List<Int>): Map<Int, DefaultWebSocketServerSession> {
	return userIds.associateWithNotNull { sessions[it] }
}

suspend inline fun DefaultWebSocketServerSession.sendMessage(
	type: NoWebSocketType,
	senderId: Int,
) = this.send("$type::$senderId")

suspend inline fun <reified T> DefaultWebSocketServerSession.sendMessage(
	type: NoWebSocketType,
	senderId: Int,
	data: T
) = this.send("$type::$senderId::${Json.encodeToString(data)}")

interface NoWebSocketType

private val CannotAccept = CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Unauthorized")

fun Application.keepAliveWebSocket() {
	routing {
		authenticate {
			webSocket("/keepAlive") {
				val principal = call.getPrincipalOrNull() ?: return@webSocket close(CannotAccept)
				val username = principal.username
				sessions[principal.userId] = this
				NoLog.info("[WS] Connect: $username")
				try {
					for (frame in incoming) {
						if (frame is Frame.Close) break
					}
				} catch (e: Exception) {
					NoLog.error(e.message, e.stackTraceToString())
				} finally {
					sessions -= principal.userId
					NoLog.info("[WS] Disconnect: $username")
				}
			}
		}
	}
}