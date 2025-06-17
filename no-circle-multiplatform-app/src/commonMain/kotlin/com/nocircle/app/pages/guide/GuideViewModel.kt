package com.nocircle.app.pages.guide

import androidx.lifecycle.viewModelScope
import com.nocircle.app.api.impls.authApi
import com.nocircle.app.ktorfitx.ktorfitx
import com.nocircle.app.ktorfitx.success
import com.nocircle.app.pages.account.login.LoginRoute
import com.nocircle.app.pages.main.MainRoute
import com.nocircle.compose.navigation.NoRoute
import com.nocircle.compose.viewmodel.NoViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class GuideViewModel : NoViewModel() {
	
	private val _navigateTo = MutableSharedFlow<NoRoute>()
	val navigateTo = _navigateTo.asSharedFlow()
	
	init {
		viewModelScope.launch {
			val verifyResult = async { verifyToken() }
			val delayResult = async { delay(2000) }
			val success = verifyResult.await()
			delayResult.await()
			_navigateTo.emit(if (success) MainRoute else LoginRoute)
		}
	}
	
	suspend fun verifyToken(): Boolean {
		val result = ktorfitx.authApi.verifyToken() ?: return networkError()
		return result.success
	}
}