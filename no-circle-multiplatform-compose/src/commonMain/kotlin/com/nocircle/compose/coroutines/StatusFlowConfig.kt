package com.nocircle.compose.coroutines

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking

abstract class StatusFlowConfig<T : Any> {
	
	abstract suspend fun getConfigFromStorage(): T
	
	abstract suspend fun setConfigToStorage(oldConfig: T, newConfig: T)
	
	private val statusFlow by lazy {
		val value = runBlocking(Dispatchers.IO) {
			getConfigFromStorage()
		}
		MutableStateFlow(value)
	}
	
	suspend fun update(value: T) {
		val oldValue = statusFlow.value
		if (oldValue == value) return
		statusFlow.value = value
		setConfigToStorage(oldValue, value)
	}
	
	suspend fun refresh() = update(getConfigFromStorage())
	
	val value: T
		get() = statusFlow.value
	
	val current: T
		@Composable
		get() = statusFlow.collectAsState().value
}