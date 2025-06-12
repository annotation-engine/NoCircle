package com.nocircle.app.pages.main

import androidx.lifecycle.viewModelScope
import com.nocircle.app.websockets.WebSocketScheduler
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel : NoViewModel() {
	
	val mainSubRoute = MutableStateFlow(MainSubRoute.HOME)
	
	val isLeftNavigationBarExpended = MutableStateFlow(false)
	
	init {
		viewModelScope.launch {
			WebSocketScheduler.keepAlive()
		}
	}
}