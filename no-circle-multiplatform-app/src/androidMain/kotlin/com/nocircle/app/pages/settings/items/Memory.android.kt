package com.nocircle.app.pages.settings.items

import android.os.Debug

private val memoryInfo = Debug.MemoryInfo()

actual suspend fun getUsedMemory(): Long {
	Debug.getMemoryInfo(memoryInfo)
	return memoryInfo.totalPss * KB
}

private const val KB = 1024L

actual suspend fun freeMemory() {
	System.gc()
}