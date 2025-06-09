package com.nocircle.server.common.websockets

interface NoWebSocketDisposer<T : Any> {
	
	fun decodeFromString(json: String): T
	
	fun dispose(targetIds: List<Int>, data: T)
}