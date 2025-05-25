package com.nocircle.compose.foundation

import androidx.compose.runtime.Composable

/**
 * Only Desktop platform
 */
@Composable
expect fun NoWindowDraggableArea(
	content: @Composable () -> Unit
)