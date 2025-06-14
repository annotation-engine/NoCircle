package com.nocircle.server.common.expends

import kotlinx.datetime.*
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

private const val DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss"

@OptIn(FormatStringsInDatetimeFormats::class)
fun Instant.formatToShanghai(pattern: String = DEFAULT_PATTERN): String {
	val localDateTime = this.toLocalDateTime(TimeZone.of("Asia/Shanghai"))
	val formatter = LocalDateTime.Format {
		byUnicodePattern(pattern)
	}
	UtcOffset(8)
	return formatter.format(localDateTime)
}