package com.nocircle.compose.desktop

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
expect fun NoTooltipArea(
	tooltip: @Composable () -> Unit,
	modifier: Modifier = Modifier,
	delayMillis: Int = 500,
	tooltipPlacement: NoTooltipPlacement = NoTooltipPlacement.CursorPoint(
		offset = DpOffset(0.dp, 16.dp)
	),
	content: @Composable () -> Unit
)

sealed interface NoTooltipPlacement {
	
	class CursorPoint(
		internal val offset: DpOffset = DpOffset.Zero,
		internal val alignment: Alignment = Alignment.BottomEnd,
		internal val windowMargin: Dp = 4.dp
	) : NoTooltipPlacement
	
	class ComponentRect(
		internal val anchor: Alignment = Alignment.BottomCenter,
		internal val alignment: Alignment = Alignment.BottomCenter,
		internal val offset: DpOffset = DpOffset.Zero
	) : NoTooltipPlacement
}