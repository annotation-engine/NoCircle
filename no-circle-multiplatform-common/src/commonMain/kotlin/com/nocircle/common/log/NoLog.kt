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
	
	private const val TAG = "NoCircleTAG"
	
	fun verbose(
		vararg messages: Any?,
		store: Boolean = false,
	) {
		this.log(TAG, LogLevel.Verbose, store, *messages)
	}
	
	fun verbose(
		store: Boolean = false,
		provider: () -> Any?
	) {
		this.log(TAG, LogLevel.Verbose, store, provider())
	}
	
	fun debug(
		vararg messages: Any?,
		store: Boolean = false,
	) {
		this.log(TAG, LogLevel.Debug, store, *messages)
	}
	
	fun debug(
		store: Boolean = false,
		provider: () -> Any?
	) {
		this.log(TAG, LogLevel.Debug, store, provider())
	}
	
	fun info(
		vararg messages: Any?,
		store: Boolean = false,
	) {
		this.log(TAG, LogLevel.Info, store, *messages)
	}
	
	fun info(
		store: Boolean = false,
		provider: () -> Any?
	) {
		this.log(TAG, LogLevel.Info, store, provider())
	}
	
	fun warn(
		vararg messages: Any?,
		store: Boolean = false,
	) {
		this.log(TAG, LogLevel.Warn, store, *messages)
	}
	
	fun warn(
		store: Boolean = false,
		provider: () -> Any?
	) {
		this.log(TAG, LogLevel.Warn, store, provider())
	}
	
	fun error(
		vararg messages: Any?,
		store: Boolean = false,
	) {
		this.log(TAG, LogLevel.Error, store, *messages)
	}
	
	fun error(
		store: Boolean = false,
		provider: () -> Any?
	) {
		this.log(TAG, LogLevel.Error, store, provider())
	}
	
	fun assert(
		vararg messages: Any?,
		store: Boolean = false,
	) {
		this.log(TAG, LogLevel.Assert, store, *messages)
	}
	
	fun assert(
		store: Boolean = false,
		provider: () -> Any?
	) {
		this.log(TAG, LogLevel.Assert, store, provider())
	}
	
	@OptIn(ExperimentalTime::class)
	private fun log(tag: String, level: LogLevel, store: Boolean, vararg messages: Any?) {
		val message = messages.joinToString()
		this.log(tag, level, message)
		if (store) {
			CoroutineScope(Dispatchers.IO).launch {
				val entity = LogEntity(
					name = tag,
					level = level.toString(),
					content = message,
					timestamp = Clock.System.now().toEpochMilliseconds()
				)
				CommonDatabase.INSTANCE.configLog().insert(entity)
			}
		}
	}
}

internal expect fun NoLog.log(tag: String, level: LogLevel, message: String)

internal enum class LogLevel {
	Verbose,
	Debug,
	Info,
	Warn,
	Error,
	Assert
}