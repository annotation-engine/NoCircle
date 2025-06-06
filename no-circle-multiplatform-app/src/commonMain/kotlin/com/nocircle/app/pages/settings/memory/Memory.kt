package com.nocircle.app.pages.settings.memory

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Memory
import androidx.compose.runtime.*
import com.nocircle.app.resources.AppString
import com.nocircle.common.resources.value
import com.nocircle.compose.complex.NoOption
import kotlinx.coroutines.delay

@Composable
fun Memory() {
	var usedMemory by remember { mutableStateOf("---") }
	LaunchedEffect(Unit) {
		while (true) {
			usedMemory = getUsedMemory().format()
			delay(1000L)
		}
	}
	NoOption(
		title = AppString.SettingsMemory.value(),
		icon = Icons.Rounded.Memory,
		subtitle = AppString.SettingsUseMemory.value(usedMemory)
	) {
		freeMemory()
	}
}

expect fun getUsedMemory(): Long

expect fun freeMemory()

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