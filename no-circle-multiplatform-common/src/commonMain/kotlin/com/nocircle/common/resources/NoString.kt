@file:OptIn(InternalCoroutinesApi::class)

package com.nocircle.common.resources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.nocircle.common.expends.format
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.readResourceBytes

interface NoString {

    val packageName: String
}

private val LoadStringJsonMutex = Mutex()

private const val STRING_FILE_PATH = "composeResources/{}.generated.resources/files/{}.json"

@OptIn(InternalResourceApi::class)
suspend fun loadStringJson(
    language: SupportLanguage,
    packageName: String,
): Map<String, String> {
    val path = STRING_FILE_PATH.format(packageName, language.language)
    val json = readResourceBytes(path).decodeToString()
    return Json.parseToJsonElement(json).jsonObject.mapValues {
        it.value.jsonPrimitive.content
    }.also {
        language.stringCacheMap[packageName] = it
    }
}

@Composable
fun NoString.value(
    vararg args: Any?
): String {
    val language = LocalSupportLanguage.current
    val value = remember(this, language) {
        rawValue(language) ?: rawValue(SupportLanguage.Chinese)!!
    }
    return value.format(*args)
}

private fun NoString.rawValue(
    language: SupportLanguage
): String? {
    val cacheMap = language.stringCacheMap[packageName] ?: runBlocking(Dispatchers.IO) {
        language.stringCacheMap[packageName] ?: LoadStringJsonMutex.withLock {
            loadStringJson(language, packageName)
        }
    }
    return cacheMap[this.toString()]
}

suspend fun NoString.getString(
    vararg args: Any?
): String {
    val language = SupportLanguage.current
    val value = getRawString(language) ?: getRawString(SupportLanguage.Chinese)!!
    return value.format(*args)
}

private suspend fun NoString.getRawString(
    language: SupportLanguage
): String? {
    val cacheMap = language.stringCacheMap[this.packageName] ?: LoadStringJsonMutex.withLock {
        language.stringCacheMap[this.packageName] ?: withContext(Dispatchers.IO) {
            loadStringJson(language, packageName)
        }
    }
    return cacheMap[this.toString()]
}