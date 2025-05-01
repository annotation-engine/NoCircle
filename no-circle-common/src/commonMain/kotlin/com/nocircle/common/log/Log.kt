package com.nocircle.common.log

object Log {
	
	private const val TAG = "NoCircleTAG"
	
	fun verbose(vararg args: Any?) {
		this.log(TAG, LogLevel.Verbose, *args)
	}
	
	fun verbose(provider: () -> Any?) {
		this.log(TAG, LogLevel.Verbose, provider())
	}
	
	fun debug(vararg args: Any?) {
		this.log(TAG, LogLevel.Debug, *args)
	}
	
	fun debug(provider: () -> Any?) {
		this.log(TAG, LogLevel.Debug, provider())
	}
	
	fun info(vararg args: Any?) {
		this.log(TAG, LogLevel.Info, *args)
	}
	
	fun info(provider: () -> Any?) {
		this.log(TAG, LogLevel.Info, provider())
	}
	
	fun warn(vararg args: Any?) {
		this.log(TAG, LogLevel.Warn, *args)
	}
	
	fun warn(provider: () -> Any?) {
		this.log(TAG, LogLevel.Warn, provider())
	}
	
	fun error(vararg args: Any?) {
		this.log(TAG, LogLevel.Error, *args)
	}
	
	fun error(provider: () -> Any?) {
		this.log(TAG, LogLevel.Error, provider())
	}
	
	fun assert(vararg args: Any?) {
		this.log(TAG, LogLevel.Assert, *args)
	}
	
	fun assert(provider: () -> Any?) {
		this.log(TAG, LogLevel.Assert, provider())
	}
}

internal expect fun Log.log(tag: String, level: LogLevel, vararg args: Any?)

internal enum class LogLevel {
	Verbose,
	Debug,
	Info,
	Warn,
	Error,
	Assert
}