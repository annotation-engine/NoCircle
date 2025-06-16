@file:Suppress("WRONG_INVOCATION_KIND")
@file:OptIn(ExperimentalContracts::class)

package com.nocircle.common.expends

import kotlinx.coroutines.sync.Mutex
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

suspend inline fun <T> Mutex.tryWithLock(
	onBusy: suspend () -> T,
	action: suspend () -> T
): T {
	contract {
		callsInPlace(action, InvocationKind.EXACTLY_ONCE)
	}
	if (!tryLock()) return onBusy()
	return try {
		action()
	} finally {
		unlock()
	}
}

suspend inline fun Mutex.tryWithLock(
	action: suspend () -> Unit
) {
	contract {
		callsInPlace(action, InvocationKind.EXACTLY_ONCE)
	}
	if (!tryLock()) return
	return try {
		action()
	} finally {
		unlock()
	}
}

val OnBusyReturnFalse = suspend { false }