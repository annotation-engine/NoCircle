package com.nocircle.service.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

object Log {
	
	private val loggers = mutableMapOf<String, Logger>()
	private const val DEFAULT_NAME = "NoCircle"
	
	fun trace(vararg args: Any?, name: String = DEFAULT_NAME) {
		logs(args, name, Level.TRACE)
	}
	
	fun debug(vararg args: Any?, name: String = DEFAULT_NAME) {
		logs(args, name, Level.DEBUG)
	}
	
	fun info(vararg args: Any?, name: String = DEFAULT_NAME) {
		logs(args, name, Level.INFO)
	}
	
	fun warn(vararg args: Any?, name: String = DEFAULT_NAME) {
		logs(args, name, Level.WARN)
	}
	
	fun error(vararg args: Any?, name: String = DEFAULT_NAME) {
		logs(args, name, Level.ERROR)
	}
	
	private fun logs(args: Array<out Any?>, name: String, level: Level) {
		val logger = loggers.getOrPut(name) { LoggerFactory.getLogger(name) }
		logger.atLevel(level).log(args.joinToString())
	}
}