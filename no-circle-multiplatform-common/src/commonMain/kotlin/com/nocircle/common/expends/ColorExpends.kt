package com.nocircle.common.expends

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
fun String.hexToColor(): Color {
	val colorLong = this.removePrefix("#").toLong(16)
	return when (this.length) {
		7 -> Color(colorLong or 0x00000000FF000000)
		9 -> Color(colorLong)
		else -> error("Illegal color type!")
	}
}

@Stable
fun Color.toHexString(): String {
	val a = (alpha * 255).toInt().toString(radix = 16)
	val r = (red * 255).toInt().toString(radix = 16)
	val g = (green * 255).toInt().toString(radix = 16)
	val b = (blue * 255).toInt().toString(radix = 16)
	return "#$a$r$g$b"
}