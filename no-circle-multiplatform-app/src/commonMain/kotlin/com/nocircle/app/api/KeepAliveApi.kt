package com.nocircle.app.api

import cn.ktorfitx.multiplatform.annotation.Api
import cn.ktorfitx.multiplatform.annotation.BearerAuth
import cn.ktorfitx.multiplatform.annotation.WebSocket
import cn.ktorfitx.multiplatform.websockets.WebSocketSessionHandler

@Api
interface KeepAliveApi {
	
	@BearerAuth
	@WebSocket("keepAlive")
	suspend fun keepAlive(handler: WebSocketSessionHandler)
}