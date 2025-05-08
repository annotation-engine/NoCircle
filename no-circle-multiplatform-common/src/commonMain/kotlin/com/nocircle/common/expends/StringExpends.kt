package com.nocircle.common.expends

import androidx.compose.ui.graphics.Color

fun String.isAlphanumeric(): Boolean {
	return this.all { it.isLetterOrDigit() }
}

fun String.isNotAlphanumeric(): Boolean {
	return this.any { !(it.isLetterOrDigit()) }
}

fun String.hexToColor(): Color {
	val colorLong = this.removePrefix("#").toLong(16)
	return when (this.length) {
		7 -> Color(colorLong or 0x00000000FF000000)
		9 -> Color(colorLong)
		else -> error("非法的颜色类型")
	}
}