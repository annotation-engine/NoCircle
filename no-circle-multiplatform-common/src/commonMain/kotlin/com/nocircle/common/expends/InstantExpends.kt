package com.nocircle.common.expends

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class, FormatStringsInDatetimeFormats::class)
fun Instant.format(zoneId: String, pattern: String): String {
	val localDateTime = this.toLocalDateTime(TimeZone.of(zoneId))
	val formatter = LocalDateTime.Format {
		byUnicodePattern(pattern)
	}
	return formatter.format(localDateTime)
}