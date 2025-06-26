package com.nocircle.compose.resources

import androidx.compose.runtime.*
import kotlinx.serialization.json.*
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.readResourceBytes
import kotlin.reflect.KClass

interface NoString

private const val STRING_FILE_PATH = "composeResources/{}.generated.resources/files/strings/{}"

private val jsonElementCacheMap = mutableMapOf<String, JsonElement>()

private val stringCacheMap = mutableMapOf<SupportedLanguage, MutableMap<String, String>>()

@OptIn(InternalResourceApi::class)
suspend fun preloadStringJsonElements(
	packageName: String,
	enum: KClass<out Enum<*>>,
	resourceName: String
) {
	val path = STRING_FILE_PATH.format(packageName, resourceName)
	val json = readResourceBytes(path).decodeToString()
	val prefix = enum.qualifiedName!!
	jsonElementCacheMap += Json.parseToJsonElement(json).jsonObject.entries.map {
		"$prefix:${it.key}" to it.value
	}
}

@Composable
fun NoString.value(
	vararg args: Any?
): String {
	val current = SupportedLanguage.current
	val value by remember(this, current) {
		derivedStateOf { this.getCacheRawString(current) }
	}
	if (args.isEmpty()) return value
	return remember(value, *args) {
		value.format(*args)
	}
}

fun NoString.getString(
	vararg args: Any?
): String {
	val current = SupportedLanguage.value
	val value = getCacheRawString(current)
	if (args.isEmpty()) return value
	return value.format(*args)
}

fun clearSupportedLanguageCache(language: SupportedLanguage) {
	stringCacheMap -= language
}

@Stable
private fun NoString.getCacheRawString(language: SupportedLanguage): String {
	val key = "${this::class.qualifiedName!!}:$this"
	val cacheMap = stringCacheMap.getOrPut(language) { mutableMapOf() }
	return cacheMap.getOrPut(key) {
		var element = jsonElementCacheMap[key] ?: return@getOrPut ""
		if (element is JsonObject) {
			element = element.jsonObject[language.language] ?: return@getOrPut ""
		}
		element.jsonPrimitive.content
	}
}

private val formatRegex = """\{(\d*)\}""".toRegex()

@Stable
private fun String.format(vararg args: Any?): String {
	if (args.isEmpty()) return this
	var autoIndex = 0
	return this.replace(formatRegex) { match ->
		val group = match.groupValues[1]
		autoIndex++
		val index = if (group.isEmpty()) autoIndex else group.toIntOrNull() ?: return@replace match.value
		if (index in 1..args.size) {
			args[index - 1].toString()
		} else {
			match.value // 保留原样
		}
	}
}