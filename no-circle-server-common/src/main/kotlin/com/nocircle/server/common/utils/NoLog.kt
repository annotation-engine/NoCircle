package com.nocircle.server.common.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

object NoLog {
	
	val NoCircleLogger: Logger = LoggerFactory.getLogger("NoCircle")
	
	fun trace(vararg args: Any?) {
		log(args, Level.TRACE)
	}
	
	fun debug(vararg args: Any?) {
		log(args, Level.DEBUG)
	}
	
	fun info(vararg args: Any?) {
		log(args, Level.INFO)
	}
	
	fun warn(vararg args: Any?) {
		log(args, Level.WARN)
	}
	
	fun error(vararg args: Any?) {
		log(args, Level.ERROR)
	}
	
	@Suppress("NOTHING_TO_INLINE")
	private inline fun log(args: Array<out Any?>, level: Level) {
		NoCircleLogger.atLevel(level).log(args.joinToString())
	}
}