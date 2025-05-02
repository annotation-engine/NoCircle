package com.nocircle.common.expends

import androidx.navigation.NavController

fun <T> NavController.getData(key: String, remove: Boolean = true): T? {
	val handle = this.previousBackStackEntry?.savedStateHandle ?: return null
	return handle.get<T>(key).also {
		if (remove) {
			handle.remove<T>(key)
		}
	}
}

fun <T> NavController.getResult(key: String, remove: Boolean = true): T? {
	val handle = this.currentBackStackEntry?.savedStateHandle ?: return null
	return handle.get<T>(key).also {
		if (remove) {
			handle.remove<T>(key)
		}
	}
}

fun NavController.navigate(
	route: String,
	data: Map<String, Any?>? = null,
	finish: Boolean = false
) {
	if (!data.isNullOrEmpty()) {
		this.currentBackStackEntry?.savedStateHandle?.let {
			data.forEach { (key, value) ->
				it[key] = value
			}
		}
	}
	this.navigate(route) {
		launchSingleTop = true
		if (finish) {
			popUpTo(currentBackStackEntry?.destination?.route ?: return@navigate) {
				inclusive = true
			}
		}
	}
}

fun NavController.popBackStack(data: Map<String, Any?>) {
	if (data.isNotEmpty()) {
		this.previousBackStackEntry?.savedStateHandle?.let {
			data.forEach { (key, value) ->
				it[key] = value
			}
		}
	}
	this.popBackStack()
}

fun NavController.popBackStack(vararg data: Pair<String, Any?>) {
	if (data.isNotEmpty()) {
		this.previousBackStackEntry?.savedStateHandle?.let {
			data.forEach { (key, value) ->
				it[key] = value
			}
		}
	}
	this.popBackStack()
}