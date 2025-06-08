package com.nocircle.common.log

import platform.Foundation.NSLog

@Suppress("NOTHING_TO_INLINE")
internal actual inline fun NoLog.log(tag: String, level: LogLevel, message: String) {
	val level = level.toString().uppercase()
	NSLog("[$level] [$tag] $message")
}