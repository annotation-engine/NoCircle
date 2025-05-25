package com.nocircle.compose.foundation

import androidx.compose.runtime.Composable

@Composable
actual fun NoWindowDraggableArea(
	content: @Composable (() -> Unit)
) {
	content()
}