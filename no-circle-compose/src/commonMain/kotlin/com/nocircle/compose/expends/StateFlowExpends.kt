package com.nocircle.compose.expends

import kotlinx.coroutines.flow.MutableStateFlow

fun MutableStateFlow<Boolean>.not(): Boolean {
	this.value = !this.value
	return this.value
}