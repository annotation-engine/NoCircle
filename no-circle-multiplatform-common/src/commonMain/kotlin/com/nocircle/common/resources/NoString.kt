package com.nocircle.common.resources

import androidx.collection.MutableIntObjectMap
import androidx.collection.mutableIntObjectMapOf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

private const val STRING_FILE_PATH = "composeResources/{}.generated.resources/files/strings.json"

private val jsonObjectCacheMap = mutableMapOf<String, JsonObject>()

private val stringCacheMap = mutableMapOf<String, MutableMap<SupportLanguage, MutableIntObjectMap<String>>>()

@OptIn(InternalResourceApi::class)
suspend fun loadJsonElementMap(
    packageName: String,
): Map<String, JsonElement> {
    val path = STRING_FILE_PATH.format(packageName)
    val json = readResourceBytes(path).decodeToString()
    return Json.parseToJsonElement(json).jsonObject.also {
        jsonObjectCacheMap[packageName] = it
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

private fun NoString.getCacheRawString(language: SupportLanguage): String {
    val hashCode = this.hashCode()
    val value = stringCacheMap[packageName]?.get(language)?.get(hashCode)
    if (value != null) return value
    val cacheMap = stringCacheMap.getOrPut(packageName) { mutableMapOf() }
        .getOrPut(language) { mutableIntObjectMapOf() }
    val elementCacheMap = jsonObjectCacheMap[packageName] ?: runBlocking(Dispatchers.IO) {
        LoadStringJsonMutex.withLock {
            jsonObjectCacheMap[packageName] ?: loadJsonElementMap(packageName)
        }
    }
    val element = elementCacheMap[this.toString()] ?: error("Unknown String: $this not in strings.json")
    return (element.jsonObject[language.language] ?: element.jsonObject[SupportLanguage.Chinese.language]!!)
        .jsonPrimitive.content.also {
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

private suspend fun NoString.getSuspendedCacheRawString(language: SupportLanguage): String {
    val hashCode = this.hashCode()
    val value = stringCacheMap[packageName]?.get(language)?.get(hashCode)
    if (value != null) return value
    val cacheMap = stringCacheMap.getOrPut(packageName) { mutableMapOf() }
        .getOrPut(language) { mutableIntObjectMapOf() }
    val elementCacheMap = jsonObjectCacheMap[packageName] ?: LoadStringJsonMutex.withLock {
        jsonObjectCacheMap[packageName] ?: loadJsonElementMap(packageName)
    }
    val element = elementCacheMap[this.toString()] ?: error("Unknown key: $this in strings.json")
    return (element.jsonObject[language.language] ?: element.jsonObject[SupportLanguage.Chinese.language]!!)
        .jsonPrimitive.content.also {
            cacheMap[hashCode] = it
        }
}

fun clearLanguageCache(language: SupportLanguage) {
    stringCacheMap.values.forEach {
        it -= language
    }
}