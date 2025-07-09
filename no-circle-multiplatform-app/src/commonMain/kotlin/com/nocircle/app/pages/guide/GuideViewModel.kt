package com.nocircle.app.pages.guide

import androidx.lifecycle.viewModelScope
import com.nocircle.app.api.impls.authApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.pages.account.login.LoginRoute
import com.nocircle.app.pages.main.MainRoute
import com.nocircle.app.resources.AppString
import com.nocircle.common.expends.success
import com.nocircle.compose.navigation.NoRoute
import com.nocircle.compose.resources.ComposeString
import com.nocircle.compose.resources.preloadStringJsonElements
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@OptIn(ExperimentalCoroutinesApi::class)
class GuideViewModel : NoViewModel() {
	
	private val _navigateTo = MutableSharedFlow<NoRoute>()
	val navigateTo = _navigateTo.asSharedFlow()
	
	init {
		viewModelScope.launch {
			val verifyResult = async { verifyToken() }
			val delayResult = async { delay(2000) }
			val preloadResult = async(Dispatchers.IO) { preloadStringJsonElements() }
			delayResult.await()
			preloadResult.await()
			val success = verifyResult.await()
			_navigateTo.emit(if (success) MainRoute else LoginRoute)
		}
	}
	
	private suspend fun verifyToken(): Boolean {
		val result = ktorfitx.authApi.verifyToken()
			.getOrNull() ?: return networkError()
		return result.success
	}
	
	private suspend fun preloadStringJsonElements() {
		preloadStringJsonElements("com.nocircle.app", AppString::class, "app-string.json")
		preloadStringJsonElements("com.nocircle.compose", ComposeString::class, "compose-string.json")
	}
}