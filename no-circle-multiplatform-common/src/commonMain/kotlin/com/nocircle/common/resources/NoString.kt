package com.nocircle.common.resources

import androidx.collection.MutableIntObjectMap
import androidx.collection.mutableIntObjectMapOf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.util.fastFlatMap
import com.nocircle.common.expends.format
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.*
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.readResourceBytes

interface NoString {
	
	val packageName: String
}

private val LoadStringJsonMutex = Mutex()

private const val STRING_FILE_PATH = "composeResources/{}.generated.resources/files/"

private val allFileNames = mutableMapOf<String, List<String>>()

private val jsonObjectCacheMap = mutableMapOf<String, Map<String, JsonElement>>()

private val stringCacheMap = mutableMapOf<String, MutableMap<SupportLanguage, MutableIntObjectMap<String>>>()

@OptIn(InternalResourceApi::class)
suspend fun loadStringJsonObject(
	packageName: String,
	fileNames: List<String> = allFileNames[packageName] ?: emptyList()
): Map<String, JsonElement> {
	return LoadStringJsonMutex.withLock {
		jsonObjectCacheMap[packageName]?.let { return it }
		if (packageName !in allFileNames) {
			allFileNames[packageName] = fileNames
		}
		fileNames.fastFlatMap { fileName ->
			val path = "${STRING_FILE_PATH}$fileName".format(packageName)
			val json = readResourceBytes(path).decodeToString()
			Json.parseToJsonElement(json).jsonObject.entries
		}.associate { it.toPair() }.also {
			jsonObjectCacheMap[packageName] = it
		}
	}
}

@Composable
fun NoString.value(
	vararg args: Any?
): String {
	val language = LocalSupportLanguage.current
	val value = remember(this, language) {
		this.getCacheRawString(language)
	}
	return value.format(*args)
}

@Stable
private fun NoString.getCacheRawString(language: SupportLanguage): String {
	val hashCode = this.hashCode()
	val value = stringCacheMap[packageName]?.get(language)?.get(hashCode)
	if (value != null) return value
	val cacheMap = stringCacheMap.getOrPut(packageName) { mutableMapOf() }
		.getOrPut(language) { mutableIntObjectMapOf() }
	val elementCacheMap = jsonObjectCacheMap[packageName] ?: runBlocking(Dispatchers.IO) {
		loadStringJsonObject(packageName)
	}
	var element = elementCacheMap[this.toString()] ?: return ""
	if (element is JsonObject) {
		element = element.jsonObject[language.language] ?: return ""
	}
	return element.jsonPrimitive.content.also {
		cacheMap[hashCode] = it
	}
}

suspend fun NoString.getString(
	vararg args: Any?
): String {
	val language = SupportLanguage.current.value
	val value = this.getSuspendedCacheRawString(language)
	return value.format(*args)
}

@Stable
private suspend fun NoString.getSuspendedCacheRawString(language: SupportLanguage): String {
	val hashCode = this.hashCode()
	val value = stringCacheMap[packageName]?.get(language)?.get(hashCode)
	if (value != null) return value
	val cacheMap = stringCacheMap.getOrPut(packageName) { mutableMapOf() }
		.getOrPut(language) { mutableIntObjectMapOf() }
	val elementCacheMap = jsonObjectCacheMap[packageName] ?: loadStringJsonObject(packageName)
	var element = elementCacheMap[this.toString()] ?: return ""
	if (element is JsonObject) {
		element = element.jsonObject[language.language] ?: return ""
	}
	return element.jsonPrimitive.content.also {
		cacheMap[hashCode] = it
	}
}

fun clearLanguageCache(language: SupportLanguage) {
	stringCacheMap.values.forEach {
		it -= language
	}
}