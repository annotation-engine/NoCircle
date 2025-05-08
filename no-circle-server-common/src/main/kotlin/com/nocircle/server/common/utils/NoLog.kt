package com.nocircle.server.common.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

object NoLog {
	
	val NoCircleLogger: Logger = LoggerFactory.getLogger("NoCircle")
	
	fun trace(vararg args: Any?) {
		log(args, Level.TRACE)
	}
	
	fun trace(provider: () -> Any?) {
		log(Level.TRACE, provider)
	}
	
	fun buildTrace(builderAction: StringBuilder.() -> Unit) {
		buildLog(Level.TRACE, builderAction)
	}
	
	fun debug(vararg args: Any?) {
		log(args, Level.DEBUG)
	}
	
	fun debug(provider: () -> Any?) {
		log(Level.DEBUG, provider)
	}
	
	fun buildDebug(builderAction: StringBuilder.() -> Unit) {
		buildLog(Level.DEBUG, builderAction)
	}
	
	fun info(vararg args: Any?) {
		log(args, Level.INFO)
	}
	
	fun info(provider: () -> Any?) {
		log(Level.INFO, provider)
	}
	
	fun buildInfo(builderAction: StringBuilder.() -> Unit) {
		buildLog(Level.INFO, builderAction)
	}
	
	fun warn(vararg args: Any?) {
		log(args, Level.WARN)
	}
	
	fun warn(provider: () -> Any?) {
		log(Level.WARN, provider)
	}
	
	fun buildWarn(builderAction: StringBuilder.() -> Unit) {
		buildLog(Level.WARN, builderAction)
	}
	
	fun error(vararg args: Any?) {
		log(args, Level.ERROR)
	}
	
	fun error(provider: () -> Any?) {
		log(Level.ERROR, provider)
	}
	
	fun buildError(builderAction: StringBuilder.() -> Unit) {
		buildLog(Level.ERROR, builderAction)
	}
	
	@Suppress("NOTHING_TO_INLINE")
	private inline fun log(args: Array<out Any?>, level: Level) {
		NoCircleLogger.atLevel(level).log(args.joinToString())
	}
	
	private inline fun log(level: Level, provider: () -> Any?) {
		NoCircleLogger.atLevel(level).log(provider().toString())
	}
	
	private inline fun buildLog(level: Level, builderAction: StringBuilder.() -> Unit) {
		NoCircleLogger.atLevel(level).log(buildString(builderAction))
	}
}