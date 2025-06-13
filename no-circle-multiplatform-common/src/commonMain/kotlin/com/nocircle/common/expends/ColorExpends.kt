package com.nocircle.common.expends

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
fun colorToHex(color: Color): String {
	val a = (color.alpha * 255).toInt().toString(radix = 16).padStart(2, '0')
	val r = (color.red * 255).toInt().toString(radix = 16).padStart(2, '0')
	val g = (color.green * 255).toInt().toString(radix = 16).padStart(2, '0')
	val b = (color.blue * 255).toInt().toString(radix = 16).padStart(2, '0')
	return "#$a$r$g$b"
}

@Stable
fun hexToColor(hex: String): Color {
	val cleanedHex = hex.removePrefix("#")
	return when (cleanedHex.length) {
		6 -> Color(cleanedHex.toLong(16) or 0xFF000000)
		8 -> Color(cleanedHex.toLong(16))
		else -> error("$hex is a illegal color type!")
	}
}