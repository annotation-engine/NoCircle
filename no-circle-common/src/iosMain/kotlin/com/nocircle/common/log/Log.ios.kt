package com.nocircle.common.log

import platform.Foundation.NSLog

internal actual fun Log.log(tag: String, level: LogLevel, vararg args: Any?) {
	val level = level.toString().uppercase()
	NSLog("[$level] [$tag] ${args.joinToString()}")
}