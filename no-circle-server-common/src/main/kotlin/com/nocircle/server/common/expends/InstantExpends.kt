package com.nocircle.server.common.expends

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.FormatAppStringInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime

@OptIn(FormatAppStringInDatetimeFormats::class)
fun Instant.format(pattern: String = Patterns.ALL_NORMAL): String {
    val localDateTime = this.toLocalDateTime(TimeZone.of("Asia/Shanghai"))
    val formatter = LocalDateTime.Format {
        byUnicodePattern(pattern)
    }
    return formatter.format(localDateTime)
}

object Patterns {

    const val ALL_NORMAL = "yyyy-MM-dd HH:mm:ss"
}