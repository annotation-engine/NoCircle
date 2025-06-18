package com.nocircle.compose.desktop

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Only Desktop platform
 */
@Composable
expect fun NoWindowDraggableArea(
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit
)