package com.nocircle.common.log

import java.util.logging.Level
import java.util.logging.Logger

private val loggerCache = mutableMapOf<String, Logger>()

@Suppress("NOTHING_TO_INLINE")
internal actual inline fun NoLog.log(tag: String, level: LogLevel, message: String) {
	val logger = loggerCache.getOrPut(tag) { Logger.getLogger(tag) }
	logger.log(level.toJavaLevel(), message)
}

private fun LogLevel.toJavaLevel(): Level = when (this) {
	LogLevel.VERBOSE -> Level.FINEST
	LogLevel.DEBUG -> Level.FINE
	LogLevel.INFO -> Level.INFO
	LogLevel.WARN -> Level.WARNING
	LogLevel.ERROR -> Level.SEVERE
	LogLevel.ASSERT -> Level.SEVERE
}