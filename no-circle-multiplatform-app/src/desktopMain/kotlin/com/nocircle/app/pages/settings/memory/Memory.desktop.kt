package com.nocircle.app.pages.settings.memory

actual suspend fun getUsedMemory(): Long {
	val runtime = Runtime.getRuntime()
	return runtime.totalMemory() - runtime.freeMemory()
}

actual suspend fun freeMemory() {
	System.gc()
}