package com.nocircle.server.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

object NoLog {
	
	val NoCircleLogger: Logger = LoggerFactory.getLogger("NoCircle")
	
	fun trace(vararg args: Any?) {
		logs(args, Level.TRACE)
	}
	
	fun trace(provider: () -> Any?) {
		logs(Level.TRACE, provider)
	}
	
	fun debug(vararg args: Any?) {
		logs(args, Level.DEBUG)
	}
	
	fun debug(provider: () -> Any?) {
		logs(Level.DEBUG, provider)
	}
	
	fun info(vararg args: Any?) {
		logs(args, Level.INFO)
	}
	
	fun info(provider: () -> Any?) {
		logs(Level.INFO, provider)
	}
	
	fun warn(vararg args: Any?) {
		logs(args, Level.WARN)
	}
	
	fun warn(provider: () -> Any?) {
		logs(Level.WARN, provider)
	}
	
	fun error(vararg args: Any?) {
		logs(args, Level.ERROR)
	}
	
	fun error(provider: () -> Any?) {
		logs(Level.ERROR, provider)
	}
	
	@Suppress("NOTHING_TO_INLINE")
	private inline fun logs(args: Array<out Any?>, level: Level) {
		NoCircleLogger.atLevel(level).log(args.joinToString())
	}
	
	private inline fun logs(level: Level, provider: () -> Any?) {
		NoCircleLogger.atLevel(level).log(provider().toString())
	}
}