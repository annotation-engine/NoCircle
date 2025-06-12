package com.nocircle.app.pages.settings.memory

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.common.resources.value
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.layout.NoOption
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Memory() {
	val coroutineScope = rememberCoroutineScope()
	NoOption(
		title = { Text(AppString.SETTINGS_MEMORY.value()) },
		icon = { NoIcon(AppIcon.Memory.value()) },
		actions = {
			var usedMemory by remember { mutableStateOf("--") }
			LaunchedEffect(Unit) {
				while (true) {
					usedMemory = getUsedMemory().format()
					delay(1000L)
				}
			}
			Text(AppString.SETTINGS_USE_MEMORY.value(usedMemory))
		}
	) {
		coroutineScope.launch {
			freeMemory()
		}
	}
}

expect suspend fun getUsedMemory(): Long

expect suspend fun freeMemory()

private const val KB = 1024.0
private const val MB = KB * 1024.0
private const val GB = MB * 1024.0

private fun Long.format(): String {
	return when {
		this < MB -> "${((this / KB) * 100L).toLong() / 100.0} KB"
		this < GB -> "${((this / MB) * 100L).toLong() / 100.0} MB"
		else -> "${((this / GB) * 100L).toLong() / 100.0} GB"
	}
}