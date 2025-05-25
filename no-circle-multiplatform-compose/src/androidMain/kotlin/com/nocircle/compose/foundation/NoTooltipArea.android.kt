package com.nocircle.compose.foundation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun NoTooltipArea(
	tooltip: @Composable (() -> Unit),
	modifier: Modifier,
	delayMillis: Int,
	tooltipPlacement: NoTooltipPlacement,
	content: @Composable (() -> Unit)
) {
	content()
}