package com.nocircle.server.common.expends

fun String.isAlphanumeric(): Boolean {
	return this.all { it.isLetterOrDigit() }
}

fun String.isNotAlphanumeric(): Boolean {
	return this.any { !(it.isLetterOrDigit()) }
}

fun String.getDisplayLength(): Long {
	return this.sumOf {
		when {
			it.isChinese() -> 2L
			else -> 1L
		}
	}
}

fun Char.isChinese(): Boolean {
	return this in '\u4e00' .. '\u9fff'
}