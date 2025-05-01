package com.nocircle.common.log

import java.util.logging.Level
import java.util.logging.Logger

private val loggerCache = mutableMapOf<String, Logger>()

internal actual fun Log.log(tag: String, level: LogLevel, vararg args: Any?) {
	val logger = loggerCache.getOrPut(tag) { Logger.getLogger(tag) }
	logger.log(level.toJavaLevel(), args.joinToString())
}

private fun LogLevel.toJavaLevel(): Level = when (this) {
	LogLevel.Verbose -> Level.FINEST
	LogLevel.Debug -> Level.FINE
	LogLevel.Info -> Level.INFO
	LogLevel.Warn -> Level.WARNING
	LogLevel.Error -> Level.SEVERE
	LogLevel.Assert -> Level.SEVERE
}