package com.nocircle.compose.resources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.InternalResourceApi

interface NoString

private val stringCacheMap = mutableMapOf<SupportLanguage, Map<NoString, String>>()

private val regex = """^(.+?)\s*:\s*(.+)$""".toRegex()

@OptIn(InternalResourceApi::class)
suspend fun loadStringConf(
    language: SupportLanguage,
    readByteArray: suspend (SupportLanguage) -> ByteArray,
    format: (String) -> NoString
): Map<NoString, String> {
    val content = readByteArray(language).decodeToString()
    return content.lines().filter { it.contains(":") }.associate {
        val values = regex.matchEntire(it)!!.groupValues
        format(values[1].trim()) to values[2].trim()
    }.also {
        stringCacheMap[language] = it
    }
}

@Composable
fun NoString.rawValue(
    readByteArray: suspend (SupportLanguage) -> ByteArray,
    format: (String) -> NoString
): String {
    val language = LocalSupportLanguage.current
    return remember(this, language) {
        stringCacheMap.getOrPut(language) {
            runBlocking(Dispatchers.IO) {
                loadStringConf(language, readByteArray, format)
            }
        }[this] ?: stringCacheMap.getOrPut(SupportLanguage.Chinese) {
            runBlocking(Dispatchers.IO) {
                loadStringConf(SupportLanguage.Chinese, readByteArray, format)
            }
        }[this]!!
    }
}

suspend fun NoString.getRawString(
    readByteArray: suspend (SupportLanguage) -> ByteArray,
    format: (String) -> NoString
): String {
    val language = SupportLanguage.current
    return stringCacheMap.getOrPut(language) {
        loadStringConf(language, readByteArray, format)
    }[this] ?: stringCacheMap.getOrPut(SupportLanguage.Chinese) {
        loadStringConf(SupportLanguage.Chinese, readByteArray, format)
    }[this]!!
}