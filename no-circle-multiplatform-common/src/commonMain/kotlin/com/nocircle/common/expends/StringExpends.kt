package com.nocircle.common.expends

import androidx.compose.runtime.Stable

@Stable
fun String.isAlphanumeric(): Boolean {
	return this.all { it.isLetterOrDigit() }
}

@Stable
fun String.isNotAlphanumeric(): Boolean {
	return this.any { !(it.isLetterOrDigit()) }
}

@Stable
fun String.getDisplayLength(): Int = this.sumOf {
	if (it.isWideChar()) TWO else ONE
}

private const val ONE = 1
private const val TWO = 2

private val formatRegex = """\{(\d*)\}""".toRegex()

@Stable
fun String.format(vararg args: Any?): String {
	if (args.isEmpty()) return this
	var autoIndex = 0
	return this.replace(formatRegex) { match ->
		val group = match.groupValues[1]
		autoIndex++
		val index = if (group.isEmpty()) autoIndex else group.toIntOrNull() ?: return@replace match.value
		if (index in 1..args.size) {
			args[index - 1].toString()
		} else {
			match.value // 保留原样
		}
	}
}