package com.nocircle.common.expends

import androidx.navigation.NavController

fun <T> NavController.setResult(key: String, value: T) {
	val handle = this.previousBackStackEntry?.savedStateHandle ?: return
	handle[key] = value
}

fun NavController.setResults(vararg values: Pair<String, *>) {
	if (values.isEmpty()) return
	val handle = this.previousBackStackEntry?.savedStateHandle ?: return
	values.forEach { (key, value) ->
		handle[key] = value
	}
}

fun <T> NavController.getResult(key: String): T? {
	val handle = this.currentBackStackEntry?.savedStateHandle ?: return null
	return handle[key]
}

fun <T> NavController.getAndRemoveResult(key: String): T? {
	val handle = this.currentBackStackEntry?.savedStateHandle ?: return null
	val value: T? = handle[key]
	handle.remove<T>(key)
	return value
}