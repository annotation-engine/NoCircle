package com.nocircle.app.pages.settings.memory

actual fun getUsedMemory(): Long {
	val runtime = Runtime.getRuntime()
	return runtime.totalMemory() - runtime.freeMemory()
}

actual fun freeMemory() {
	System.gc()
}