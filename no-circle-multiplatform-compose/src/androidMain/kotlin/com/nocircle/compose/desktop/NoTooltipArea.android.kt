package com.nocircle.compose.desktop

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual inline fun NoTooltipArea(
	tooltip: @Composable (() -> Unit),
	modifier: Modifier,
	delayMillis: Int,
	tooltipPlacement: NoTooltipPlacement,
	content: @Composable (() -> Unit)
) {
	content()
}