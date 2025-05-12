package com.nocircle.app

import java.io.File

fun generateColorSchemeGroupCodes(path: String, name: String) {
	val codes = File(path).readLines()
	val colorSchemes = mutableMapOf<String, MutableMap<String, String>>()
	codes.filter { it.startsWith("val") }.forEach {
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
		if (key == null) return@forEach
		val map = colorSchemes.getOrPut(key.second) { mutableMapOf() }
		map[name.removeSuffix(key.first)] = code
	}
	val code = buildString {
		append("import androidx.compose.material3.darkColorScheme\n")
		append("import androidx.compose.ui.graphics.Color\n\n")
		append("data object $name : ColorSchemeGroup {\n\n")
		colorSchemes.forEach { (key, value) ->
			append("\toverride val $key by lazy {\n")
			if (key.contains("Light")) {
				append("\t\tlightColorScheme(\n")
			} else {
				append("\t\tdarkColorScheme(\n")
			}
			value.forEach { (name, code) ->
				append("\t\t\t$name = $code,\n")
			}
			append("\t\t)\n\t}\n\n")
		}
		this.delete(this.length - 1, this.length)
		append("}")
	}
	println(code)
}