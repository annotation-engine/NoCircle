package com.nocircle.compose.animation

import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import java.awt.GraphicsEnvironment
import java.awt.MouseInfo
import java.awt.Rectangle

@Composable
fun animateWindowStateAsState(
	targetValue: DpSize,
	finishedListener: ((DpSize) -> Unit)? = null,
): State<WindowState> {
	val size by animateDpSizeAsState(
		targetValue = targetValue,
		finishedListener = finishedListener
	)
	return remember(size) {
		derivedStateOf {
			WindowState(
				position = calcWindowPosition(size),
				size = size,
			)
		}
	}
}

@Stable
private fun calcWindowPosition(size: DpSize): WindowPosition {
	val screenBounds = getCursorScreenBounds()
	val x = screenBounds.x + ((screenBounds.width - size.width.value) / 2f)
	val y = screenBounds.y + ((screenBounds.height - size.height.value) / 2f)
	return WindowPosition(
		x = x.dp,
		y = y.dp,
	)
}

@Stable
private fun getCursorScreenBounds(): Rectangle {
	val mouseLocation = MouseInfo.getPointerInfo().location
	val environment = GraphicsEnvironment.getLocalGraphicsEnvironment()
	environment.screenDevices.forEach {
		val bounds = it.defaultConfiguration.bounds
		if (mouseLocation in bounds) {
			return bounds
		}
	}
	return environment.defaultScreenDevice.defaultConfiguration.bounds
}