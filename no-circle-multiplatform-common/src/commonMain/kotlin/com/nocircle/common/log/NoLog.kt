package com.nocircle.common.log

import com.nocircle.common.room.CommonDatabase
import com.nocircle.common.room.entity.LogEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object NoLog {
	
	private const val TAG = "NoCircleTag"
	
	fun verbose(
		vararg messages: Any?,
		store: Boolean = false,
	) {
		this.log(LogLevel.VERBOSE, store, *messages)
	}
	
	fun debug(
		vararg messages: Any?,
		store: Boolean = false,
	) {
		this.log(LogLevel.DEBUG, store, *messages)
	}
	
	fun info(
		vararg messages: Any?,
		store: Boolean = false,
	) {
		this.log(LogLevel.INFO, store, *messages)
	}
	
	fun warn(
		vararg messages: Any?,
		store: Boolean = false,
	) {
		this.log(LogLevel.WARN, store, *messages)
	}
	
	fun error(
		vararg messages: Any?,
		store: Boolean = false,
	) {
		this.log(LogLevel.ERROR, store, *messages)
	}
	
	fun assert(
		vararg messages: Any?,
		store: Boolean = false,
	) {
		this.log(LogLevel.ASSERT, store, *messages)
	}
	
	@OptIn(ExperimentalTime::class)
	private fun log(level: LogLevel, store: Boolean, vararg messages: Any?) {
		val message = messages.joinToString()
		this.log(TAG, level, message)
		if (store) {
			CoroutineScope(Dispatchers.IO).launch {
				val entity = LogEntity(
					name = TAG,
					level = level.toString(),
					content = message,
					timestamp = Clock.System.now().toEpochMilliseconds()
				)
				CommonDatabase.INSTANCE.getLogDao().insert(entity)
			}
		}
	}
}

internal expect inline fun NoLog.log(tag: String, level: LogLevel, message: String)

internal enum class LogLevel {
	VERBOSE,
	DEBUG,
	INFO,
	WARN,
	ERROR,
	ASSERT
}