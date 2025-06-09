package com.nocircle.server.app.websockets.friend

import com.nocircle.server.common.websockets.NoWebSocketDisposer

class FriendAddRequestDisposer : NoWebSocketDisposer<String> {
	
	override fun decodeFromString(json: String): String {
		return json
	}
	
	override fun dispose(targetIds: List<Int>, data: String) {
		TODO("Not yet implemented")
	}
	
}