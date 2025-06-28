package com.nocircle.common.expends

fun String.isLowerCases(): Boolean {
	return this.all { it.isLowerCase() }
}

fun String.isUpperCases(): Boolean {
	return this.all { it.isUpperCase() }
}

fun String.isDigits(): Boolean {
	return this.all { it.isDigit() }
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