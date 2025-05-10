package com.nocircle.common.log

import android.util.Log as AndroidLog

/**
 * Android
 */
internal actual fun NoLog.log(tag: String, level: LogLevel, message: String) {
	AndroidLog.println(level.ordinal + 2, tag, message)
}