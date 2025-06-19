package com.nocircle.common.coroutines

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.reflect.KFunction

object FunctionLocker {
	
	private val mutexMap = mutableMapOf<KFunction<*>, Mutex>()
	
	private val mutex = Mutex()
	
	suspend fun tryWithLock(
		key: KFunction<*>,
		action: suspend () -> Unit
	) {
		val mutex = mutex.withLock {
			mutexMap.getOrPut(key) { Mutex() }
		}
		if (!mutex.tryLock()) return
		return try {
			action()
		} finally {
			mutex.unlock()
		}
	}
	
	suspend fun <R> tryWithLock(
		key: KFunction<*>,
		onBusy: suspend () -> R,
		action: suspend () -> R
	): R {
		val mutex = mutex.withLock {
			mutexMap.getOrPut(key) { Mutex() }
		}
		if (!mutex.tryLock()) return onBusy()
		return try {
			action()
		} finally {
			mutex.unlock()
		}
	}
}

val OnBusyReturnFalse = suspend { false }