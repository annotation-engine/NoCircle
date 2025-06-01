@file:OptIn(InternalAPI::class)

package com.nocircle.common.resources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.nocircle.common.expends.format
import io.ktor.utils.io.*
import io.ktor.utils.io.locks.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.readResourceBytes

interface NoString {

    val packageName: String
}

private val LoadStringJsonLock = SynchronizedObject()

private const val PATH = "composeResources/{}.generated.resources/files/{}.json"

@OptIn(InternalResourceApi::class)
suspend fun loadStringJson(
    language: SupportLanguage,
    packageName: String,
): Map<String, String> {
    val json = readResourceBytes(PATH.format(packageName, language.language)).decodeToString()
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
    val cacheMap = language.stringCacheMap[this.packageName] ?: synchronized(LoadStringJsonLock) {
        language.stringCacheMap[this.packageName] ?: runBlocking(Dispatchers.IO) {
            loadStringJson(language, packageName)
        }
    }
    return cacheMap[this.toString()]
}

fun NoString.getString(
    vararg args: Any?
): String {
    val language = SupportLanguage.current
    val value = getRawString(language) ?: getRawString(SupportLanguage.Chinese)!!
    return value.format(*args)
}

private fun NoString.getRawString(
    language: SupportLanguage
): String? {
    val cacheMap = language.stringCacheMap[this.packageName] ?: synchronized(LoadStringJsonLock) {
        language.stringCacheMap[this.packageName] ?: runBlocking(Dispatchers.IO) {
            loadStringJson(language, packageName)
        }
    }
    return cacheMap[this.toString()]
}