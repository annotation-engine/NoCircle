package com.nocircle.common.log

import android.util.Log as AndroidLog

/**
 * Android
 */
internal actual fun Log.log(tag: String, level: LogLevel, vararg args: Any?) {
	AndroidLog.println(level.ordinal + 2, tag, args.joinToString(", "))
}