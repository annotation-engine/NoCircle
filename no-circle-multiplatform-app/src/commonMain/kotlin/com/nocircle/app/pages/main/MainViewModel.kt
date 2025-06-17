package com.nocircle.app.pages.main

import androidx.lifecycle.viewModelScope
import com.nocircle.app.api.impls.keepAliveApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.common.log.NoLog
import com.nocircle.common.websocket.NoWebSocketType
import com.nocircle.common.websocket.WebSocketScheduler
import com.nocircle.compose.viewmodel.NoViewModel
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.time.Duration.Companion.seconds

class MainViewModel : NoViewModel() {
	
	val mainSubRoute = MutableStateFlow(MainSubRoute.HOME)
	val isLeftNavigationBarExpended = MutableStateFlow(false)
	
	private val reconnectDurationRange = 0.seconds..30.seconds
	
	init {
		viewModelScope.launch(Dispatchers.IO) {
			keepAlive()
		}
	}
	
	private suspend fun keepAlive() {
		var attempt = 0
		var close = false
		while (!close) {
			try {
				ktorfitx.keepAliveApi.keepAlive {
					attempt = 0
					NoLog.info("WebSocket connected.")
					for (frame in incoming) {
						when (frame) {
							is Frame.Text -> {
								WebSocketScheduler.scheduleText(
									frame = frame,
									findWebSocketType = { type -> WebSocketType.entries.find { it.name == type } }
								)
							}
							
							is Frame.Close -> {
								close = true
								break
							}
							
							else -> continue
						}
					}
				}
			} catch (_: Exception) {
				NoLog.error("WebSocket unconnected.")
			}
			attempt++
			val duration = 2.0.pow(attempt).seconds.coerceIn(reconnectDurationRange)
			delay(duration)
			NoLog.info("WebSocket reconnected: $attempt, next duration: ${(duration * 2).coerceIn(reconnectDurationRange)}")
		}
	}
}

enum class WebSocketType : NoWebSocketType {
	REFRESH_FRIEND_SENT_REQUEST,
	REFRESH_FRIEND_RECEIVED_REQUEST
}