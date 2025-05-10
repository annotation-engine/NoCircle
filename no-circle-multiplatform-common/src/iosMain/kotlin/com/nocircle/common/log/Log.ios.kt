package com.nocircle.common.log

import platform.Foundation.NSLog

internal actual fun NoLog.log(tag: String, level: LogLevel, message: String) {
	val level = level.toString().uppercase()
	NSLog("[$level] [$tag] $message")
}