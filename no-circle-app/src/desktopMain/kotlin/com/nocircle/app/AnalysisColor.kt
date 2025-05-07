package com.nocircle.app

import androidx.compose.ui.util.fastForEach
import java.io.File

fun analysisColor() {
	val codes = File("/Users/cooder/Code/Project/NoCircle/no-circle-app/src/commonMain/kotlin/com/nocircle/app/theme/colors/Color.kt").readLines()
	val colorSchemes = mutableMapOf<String, MutableMap<String, String>>()
	codes.filter { it.startsWith("val") }.fastForEach {
		val split = it.removePrefix("val ").split(" = ")
		val name = split[0]
		val code = split[1]
		val key = when {
			name.contains("LightMediumContrast") -> "LightMediumContrast" to "lightMediumContrast"
			name.contains("LightHighContrast") -> "LightHighContrast" to "lightHighContrast"
			name.contains("Light") -> "Light" to "lightStandardContrast"
			name.contains("DarkMediumContrast") -> "DarkMediumContrast" to "darkMediumContrast"
			name.contains("DarkHighContrast") -> "DarkHighContrast" to "darkHighContrast"
			name.contains("Dark") -> "Dark" to "darkStandardContrast"
			else -> null
		}
		if (key == null) return@fastForEach
		val map = colorSchemes.getOrPut(key.second) { mutableMapOf() }
		map[name.removeSuffix(key.first)] = code
	}
	var output = "class DefaultColorSchemeGroup : ColorSchemeGroup {\n"
	colorSchemes.forEach { (key, value) ->
		output += "\toverride val $key by lazy {\n"
		output += if (key.contains("Light")) {
			"\t\tlightColorScheme(\n"
		} else {
			"\t\tdarkColorScheme(\n"
		}
		value.forEach { (name, code) ->
			output += "\t\t\t$name = $code,\n"
		}
		output += "\t\t)\n\t}\n\n"
	}
	output += "}"
	println(output)
}