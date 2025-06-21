package com.nocircle.app.pages.main

import androidx.lifecycle.viewModelScope
import com.nocircle.app.api.impls.keepAliveApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.resources.AppString
import com.nocircle.app.theme.scheme.ColorSchemeConfig
import com.nocircle.app.theme.type.FontWeightType
import com.nocircle.app.theme.type.RoundedCornerType
import com.nocircle.common.log.NoLog
import com.nocircle.common.websocket.WebSocketScheduler
import com.nocircle.compose.resources.NoIconType
import com.nocircle.compose.resources.SupportedLanguage
import com.nocircle.compose.viewmodel.NoViewModel
import com.nocircle.shared.websocket.WebSocketType
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.pow
import kotlin.time.Duration.Companion.seconds

class MainViewModel : NoViewModel() {
	
	val mainSubRoute = MutableStateFlow(MainSubRoute.HOME)
	val isLeftNavigationBarExpended = MutableStateFlow(false)
	
	private val reconnectDurationRange = 0.seconds..30.seconds
	private var close = false
	
	init {
		viewModelScope.launch(Dispatchers.IO) {
			async { keepAlive() }
			loadColorScheme()
		}
	}
	
	private suspend fun keepAlive() {
		var attempt = 0
		while (!close) {
			try {
				ktorfitx.keepAliveApi.keepAlive {
					if (attempt > 0) {
						showNoSnackbar(AppString.MAIN_WEBSOCKET_ONLINE)
					}
					attempt = 0
					for (frame in incoming) {
						if (close) break
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
				showNoErrorSnackbar(AppString.MAIN_WEBSOCKET_OFFLINE)
			}
			val duration = 2.0.pow(attempt).seconds.coerceIn(reconnectDurationRange)
			delay(duration)
			NoLog.info("WebSocket reconnected: $attempt")
		}
	}
	
	private suspend fun loadColorScheme() {
		ColorSchemeConfig.refresh()
		SupportedLanguage.refresh()
		NoIconType.refresh()
		RoundedCornerType.refresh()
		FontWeightType.refresh()
	}
	
	override fun onCleared() {
		super.onCleared()
		close = true
	}
}