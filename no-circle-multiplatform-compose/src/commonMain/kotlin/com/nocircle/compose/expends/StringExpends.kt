package com.nocircle.compose.expends

import androidx.compose.runtime.Stable
import com.nocircle.compose.expends.isWideChar

@Stable
fun String.getDisplayLength(): Int = this.sumOf {
	if (it.isWideChar()) TWO else ONE
}

private const val ONE = 1
private const val TWO = 2