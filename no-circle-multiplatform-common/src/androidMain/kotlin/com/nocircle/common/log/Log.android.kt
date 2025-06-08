package com.nocircle.common.log

import android.util.Log as AndroidLog

/**
 * Android
 */
@Suppress("NOTHING_TO_INLINE")
internal actual inline fun NoLog.log(tag: String, level: LogLevel, message: String) {
	AndroidLog.println(level.ordinal + 2, tag, message)
}