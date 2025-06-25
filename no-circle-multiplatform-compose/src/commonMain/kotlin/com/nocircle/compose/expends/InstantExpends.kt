package com.nocircle.compose.expends

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.nocircle.common.expends.format
import com.nocircle.compose.time.TimeZoneId
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Composable
fun Instant.format(
	pattern: String = DateTimePatterns.DATE_TIME
): String {
	val zoneId = TimeZoneId.current.zoneId
	return remember(zoneId, this, pattern) {
		this.format(zoneId, pattern)
	}
}

object DateTimePatterns {
	
	const val DATE_TIME = "yyyy-MM-dd HH:mm:ss"
	
	const val DATE = "yyyy-MM-dd"
	
	const val TIME = "HH:mm:ss"
}