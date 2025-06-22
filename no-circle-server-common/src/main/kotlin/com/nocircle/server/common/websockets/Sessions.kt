package com.nocircle.server.common.websockets

import com.nocircle.shared.websocket.WebSocketType
import io.ktor.websocket.*
import kotlinx.serialization.json.Json

suspend inline fun sendToReceiver(
	type: WebSocketType,
	senderId: Int,
	receiverId: Int
) {
	val sessions = sessions[receiverId] ?: return
	if (sessions.isEmpty()) return
	val content = "$type::$senderId"
	sessions.forEach {
		it.send(content)
	}
}

suspend inline fun <reified T> sendToReceiver(
	type: WebSocketType,
	senderId: Int,
	receiverId: Int,
	value: T
) {
	val sessions = sessions[receiverId] ?: return
	if (sessions.isEmpty()) return
	val json = Json.encodeToString(value)
	val content = "$type::$senderId::$json"
	sessions.forEach {
		it.send(content)
	}
}