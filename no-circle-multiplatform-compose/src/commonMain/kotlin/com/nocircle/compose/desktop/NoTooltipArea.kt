package com.nocircle.compose.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
expect fun NoTooltipArea(
	tooltip: @Composable () -> Unit,
	modifier: Modifier = Modifier,
	delayMillis: Int = 500,
	tooltipPlacement: NoTooltipPlacement = NoTooltipPlacement.CursorPoint(
		offset = DpOffset(Dp.Hairline, 16.dp)
	),
	content: @Composable () -> Unit
)

@Composable
fun TooltipText(
	text: String,
) {
	Text(
		text = text,
		modifier = Modifier
			.clip(MaterialTheme.shapes.medium)
			.shadow(
				elevation = 8.dp,
			)
			.border(
				width = 1.dp,
				color = BorderColor,
				shape = MaterialTheme.shapes.medium
			)
			.background(
				color = BackgroundColor,
				shape = MaterialTheme.shapes.medium
			)
			.padding(
				horizontal = 12.dp,
				vertical = 8.dp
			),
		color = Color.White,
		style = MaterialTheme.typography.bodyMedium,
		textAlign = TextAlign.Center
	)
}

private val BorderColor = Color(0xFF2B2D31)
private val BackgroundColor = Color(0xFF25272C)

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