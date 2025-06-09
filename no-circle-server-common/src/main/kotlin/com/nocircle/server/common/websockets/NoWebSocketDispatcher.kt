package com.nocircle.server.common.websockets

import com.nocircle.server.common.expends.associateWithNotNull
import com.nocircle.server.common.log.NoLog
import com.nocircle.server.common.model.noPrincipal
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds


private val sessions = mutableMapOf<Int, DefaultWebSocketServerSession>()

fun getSession(userId: Int): DefaultWebSocketServerSession? {
	return sessions[userId]
}

fun getSessions(userIds: List<Int>): Map<Int, DefaultWebSocketServerSession> {
	return userIds.associateWithNotNull { sessions[it] }
}

private val CannotAccept = CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Unauthorized")

private val webSockets = mutableMapOf<String, NoWebSocketDisposer<*>>()

private const val MAX_FRAME_SIZE = 10 * 1024 * 1024L    // 10MB

fun Application.webSocketDispatch(
	scope: MutableMap<String, NoWebSocketDisposer<*>>.() -> Unit
) {
	install(WebSockets) {
		pingPeriod = 15.seconds
		timeout = 15.seconds
		maxFrameSize = MAX_FRAME_SIZE
		masking = false
	}
	webSockets.apply {
		scope()
		total()
	}
	routing {
		authenticate {
			webSocket("/dispatch") {
				val principal = call.noPrincipal ?: return@webSocket close(CannotAccept)
				val username = principal.username
				NoLog.info("[WS] Connect: $username")
				sessions[principal.userId] = this
				try {
					for (frame in incoming) {
						if (frame is Frame.Text) {
							val model = Json.decodeFromString<DispatchModel>(frame.readText())
							
							@Suppress("UNCHECKED_CAST")
							val disposer = webSockets[model.type] as? NoWebSocketDisposer<Any> ?: continue
							val data = disposer.decodeFromString(model.data)
							disposer.dispose(model.targetIds, data)
						}
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

private fun MutableMap<String, NoWebSocketDisposer<*>>.total() {
	this.keys.forEach { key ->
		NoLog.info("[WS] - $key")
	}
	NoLog.info("[WS] [TOTAL] ${this.keys.size}")
}

@Serializable
private data class DispatchModel(
	val targetIds: List<Int>,
	val type: String,
	val data: String
)