package com.nocircle.server.common.expends

fun String.isAlphanumeric(): Boolean {
	return this.all { it.isLetterOrDigit() }
}

fun String.isNotAlphanumeric(): Boolean {
	return this.any { !(it.isLetterOrDigit()) }
}

fun String.getDisplayLength(): Int = this.sumOf {
	if (it.isWideChar()) TWO else ONE
}

private const val ONE = 1
private const val TWO = 2