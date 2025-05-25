package com.nocircle.compose.desktop

import androidx.compose.runtime.Composable

@Composable
actual inline fun NoWindowDraggableArea(
	content: @Composable (() -> Unit)
) {
	content()
}