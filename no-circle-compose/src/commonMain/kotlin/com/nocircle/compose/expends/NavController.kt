package com.nocircle.compose.expends

import androidx.navigation.NavController

fun <T> NavController.getAndRemoveResult(key: String): T? {
	val handle = this.currentBackStackEntry?.savedStateHandle
	val value = handle?.get<T>(key)
	handle?.remove<String>(key)
	return value
}

fun <T> NavController.getResult(key: String): T? {
	val handle = this.currentBackStackEntry?.savedStateHandle
	return handle?.get<T>(key)
}

fun <T> NavController.setResult(key: String, value: T) {
	this.previousBackStackEntry
		?.savedStateHandle
		?.set(key, value)
}