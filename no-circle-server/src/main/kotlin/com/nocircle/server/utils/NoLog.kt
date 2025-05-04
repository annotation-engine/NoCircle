package com.nocircle.server.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

object NoLog {
	
	private val loggerCaches = mutableMapOf<String, Logger>()
	
	private const val DEFAULT_NAME = "NoCircle"
	
	fun trace(vararg args: Any?, name: String = DEFAULT_NAME) {
		logs(args, name, Level.TRACE)
	}
	
	fun trace(name: String = DEFAULT_NAME, provider: () -> Any?) {
		logs(name, Level.TRACE, provider)
	}
	
	fun debug(vararg args: Any?, name: String = DEFAULT_NAME) {
		logs(args, name, Level.DEBUG)
	}
	
	fun debug(name: String = DEFAULT_NAME, provider: () -> Any?) {
		logs(name, Level.DEBUG, provider)
	}
	
	fun info(vararg args: Any?, name: String = DEFAULT_NAME) {
		logs(args, name, Level.INFO)
	}
	
	fun info(name: String = DEFAULT_NAME, provider: () -> Any?) {
		logs(name, Level.INFO, provider)
	}
	
	fun warn(vararg args: Any?, name: String = DEFAULT_NAME) {
		logs(args, name, Level.WARN)
	}
	
	fun warn(name: String = DEFAULT_NAME, provider: () -> Any?) {
		logs(name, Level.WARN, provider)
	}
	
	fun error(vararg args: Any?, name: String = DEFAULT_NAME) {
		logs(args, name, Level.ERROR)
	}
	
	fun error(name: String = DEFAULT_NAME, provider: () -> Any?) {
		logs(name, Level.ERROR, provider)
	}
	
	@Suppress("NOTHING_TO_INLINE")
	private inline fun logs(args: Array<out Any?>, name: String, level: Level) {
		val logger = loggerCaches.getOrPut(name) { LoggerFactory.getLogger(name) }
		logger.atLevel(level).log(args.joinToString())
	}
	
	private inline fun logs(name: String, level: Level, provider: () -> Any?) {
		val logger = loggerCaches.getOrPut(name) { LoggerFactory.getLogger(name) }
		logger.atLevel(level).log(provider().toString())
	}
}