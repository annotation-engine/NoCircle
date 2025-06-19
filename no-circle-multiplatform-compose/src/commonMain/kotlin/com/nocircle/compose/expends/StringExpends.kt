package com.nocircle.compose.expends

import androidx.compose.runtime.Stable

@Stable
fun String.getDisplayLength(): Int = this.sumOf {
	if (it.isWideChar()) 2 else 1
}