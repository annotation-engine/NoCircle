package com.nocircle.app

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import java.awt.Dimension

fun main() = application {
	Window(
		onCloseRequest = ::exitApplication,
		state = WindowState(
			width = 450.dp,
			height = 700.dp,
			position = WindowPosition.Aligned(Alignment.Center)
		),
		title = "",
		resizable = false
	) {
		LaunchedEffect(Unit) {
			window.rootPane.putClientProperty("apple.awt.fullWindowContent", true)
			window.rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
			window.minimumSize = Dimension(450, 700)
		}
		App()
	}
}