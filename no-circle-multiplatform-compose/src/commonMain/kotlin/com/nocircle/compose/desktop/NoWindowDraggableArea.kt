package com.nocircle.compose.desktop

import androidx.compose.runtime.Composable

/**
 * Only Desktop platform
 */
@Composable
expect fun NoWindowDraggableArea(
	content: @Composable () -> Unit
)