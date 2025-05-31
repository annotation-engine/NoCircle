package com.nocircle.app.resources

import androidx.compose.runtime.Composable
import com.nocircle.app.generated.resources.Res
import com.nocircle.common.expends.format
import com.nocircle.compose.resources.*

private val readByteArray: suspend (SupportLanguage) -> ByteArray = {
    Res.readBytes("files/${it.language}.conf")
}

private val format: (String) -> NoString = { key ->
    AppString.entries.first { it.name == key }
}

suspend fun loadStringConf(language: SupportLanguage) {
    loadStringConf(
        language = language,
        readByteArray = readByteArray,
        format = format
    )
}

suspend fun AppString.getString(vararg args: Any?): String {
    return this.getRawString(
        readByteArray = readByteArray,
        format = format
    ).format(*args)
}

@Composable
fun AppString.value(vararg args: Any?): String {
    return this.rawValue(
        readByteArray = readByteArray,
        format = format
    ).format(*args)
}