package com.nocircle.common.expends

fun String.isAlphanumeric(): Boolean {
	return this.all { it.isLetterOrDigit() }
}

fun String.isNotAlphanumeric(): Boolean {
	return this.any { !(it.isLetterOrDigit()) }
}

fun String.findIndices(text: String, ignoreCase: Boolean = false): List<IntRange> {
	val indices = mutableListOf<IntRange>()
	var index = this.indexOf(text, ignoreCase = ignoreCase)
	while (index != -1) {
		indices += index..index + text.lastIndex
		index = this.indexOf(text, index + 1, ignoreCase)
	}
	return indices
}