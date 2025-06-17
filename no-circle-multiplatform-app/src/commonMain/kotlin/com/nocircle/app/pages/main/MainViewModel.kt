package com.nocircle.app.pages.main

import androidx.lifecycle.viewModelScope
import com.nocircle.app.api.impls.keepAliveApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.resources.AppString
import com.nocircle.common.log.NoLog
import com.nocircle.common.websocket.WebSocketScheduler
import com.nocircle.compose.resources.getString
import com.nocircle.compose.viewmodel.NoViewModel
import com.nocircle.shared.websocket.WebSocketType
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
			showNoSnackbar(AppString.LOGIN_SUCCESS.getString())
			keepAlive()
		}
	}
	
	private suspend fun keepAlive() {
		var attempt = 0
		var close = false
		while (!close) {
			try {
				ktorfitx.keepAliveApi.keepAlive {
					if (attempt > 0) {
						showNoSnackbar("您已上线")
					}
					attempt = 0
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
			}
			attempt++
			if (attempt == 1) {
				showNoErrorSnackbar("您已掉线，请检查网络是否正常")
			}
			val duration = 2.0.pow(attempt).seconds.coerceIn(reconnectDurationRange)
			delay(duration)
			NoLog.info("WebSocket reconnected: $attempt, next duration: ${(duration * 2).coerceIn(reconnectDurationRange)}")
		}
	}
}