package com.nocircle.compose.desktop

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual inline fun NoWindowDraggableArea(
	modifier: Modifier,
	content: @Composable (() -> Unit)
) {
	content()
}