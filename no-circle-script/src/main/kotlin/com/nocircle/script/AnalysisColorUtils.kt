package com.nocircle.script

import java.io.File

fun generateColorSchemeGroupCodes(path: String, name: String): String {
	val codes = File(path).readLines()
	val colorSchemes = mutableMapOf<String, MutableMap<String, String>>()
	codes.filter { it.startsWith("val") }.forEach {
		val split = it.removePrefix("val ").split(" = ")
		val name = split[0]
		val code = split[1]
		val key = when {
			"LightMediumContrast" in name -> "LightMediumContrast" to "lightMediumContrast"
			"LightHighContrast" in name -> "LightHighContrast" to "lightHighContrast"
			"Light" in name -> "Light" to "lightStandardContrast"
			"DarkMediumContrast" in name -> "DarkMediumContrast" to "darkMediumContrast"
			"DarkHighContrast" in name -> "DarkHighContrast" to "darkHighContrast"
			"Dark" in name -> "Dark" to "darkStandardContrast"
			else -> null
		}
		if (key == null) return@forEach
		val map = colorSchemes.getOrPut(key.second) { mutableMapOf() }
		map[name.removeSuffix(key.first)] = code
	}
	return buildString {
		append("import androidx.compose.material3.darkColorScheme\n")
		append("import androidx.compose.ui.graphics.Color\n")
		val themeName = camelToSnakeCaseFull(name)
		append("import com.nocircle.app.generated.resources.Res\n")
		append("import com.nocircle.app.generated.resources.appearance_theme_$themeName\n\n")
		append("object ${name}ColorSchemeGroup : ColorSchemeGroup {\n\n")
		append("\toverride val name = Res.string.appearance_theme_$themeName\n\n")
		colorSchemes.forEach { (key, value) ->
			append("\toverride val $key by lazy {\n")
			if ("Light" in key) {
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
}

private fun camelToSnakeCaseFull(input: String): String {
	return input.replace(Regex("([a-z0-9])([A-Z])"), "$1_$2")
		.replace(Regex("([A-Z])([A-Z][a-z])"), "$1_$2")
		.lowercase()
}