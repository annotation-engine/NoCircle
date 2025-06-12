package com.nocircle.app.api

import cn.vividcode.multiplatform.ktorfitx.annotation.Api
import cn.vividcode.multiplatform.ktorfitx.annotation.BearerAuth
import cn.vividcode.multiplatform.ktorfitx.websockets.WebSocket
import cn.vividcode.multiplatform.ktorfitx.websockets.WebSocketSessionHandler

@Api
interface KeepAliveApi {
	
	@BearerAuth
	@WebSocket("keepAlive")
	suspend fun keepAlive(handler: WebSocketSessionHandler)
}