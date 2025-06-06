package com.nocircle.compose.complex

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.nocircle.common.windowsize.WindowWidthSizes
import com.nocircle.compose.range.DpRange

@Composable
fun NoSplitLayout(
	contentWidth: Dp,
	onContentWidthChange: (Dp) -> Unit,
	expended: @Composable () -> Unit,
	modifier: Modifier = Modifier,
	contentWidthRange: DpRange = NoSplitLayoutDefaults.ContentWidthRange,
	expendedMinWidth: Dp = NoSplitLayoutDefaults.ExpendedMinWidth,
	content: @Composable BoxScope.(isCompat: Boolean) -> Unit,
) {
	val isCompact = WindowWidthSizes.isCompact
	var width by remember { mutableStateOf(Dp.Hairline) }
	val density = LocalDensity.current
	LaunchedEffect(width) {
		if (!isCompact) {
			val newContentWidth = width - expendedMinWidth - 1.dp
			if (contentWidth > newContentWidth) {
				onContentWidthChange(newContentWidth)
			}
		}
	}
	Box(
		modifier = modifier
			.fillMaxSize()
			.onSizeChanged {
				width = with(density) { it.width.toDp() }
			}
	) {
		Box(
			modifier = Modifier
				.fillMaxHeight()
				.then(if (isCompact) Modifier.fillMaxWidth() else Modifier.width(contentWidth))
		) {
			content(isCompact)
		}
		if (!isCompact) {
			Box(
				modifier = Modifier
					.padding(
						start = contentWidth + 1.dp
					)
					.fillMaxSize()
			) {
				expended()
			}
			val currentContentWidth by rememberUpdatedState(contentWidth)
			val interactionSource = remember { MutableInteractionSource() }
			val isHovered by interactionSource.collectIsHoveredAsState()
			var isDragging by remember { mutableStateOf(false) }
			val isHighlight by remember(isHovered || isDragging) {
				derivedStateOf { isHovered || isDragging }
			}
			val paddingHorizontal by animateDpAsState(
				targetValue = if (isHighlight) 3.25.dp else 4.dp
			)
			val lineColor by animateColorAsState(
				targetValue = if (isHighlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
			)
			Box(
				modifier = Modifier
					.offset(x = contentWidth - 4.dp)
					.width(9.dp)
					.fillMaxHeight()
					.pointerResizeHorizontalHoverIcon()
					.hoverable(interactionSource)
					.pointerInput(width, expendedMinWidth, contentWidthRange) {
						var contentWidth = Dp.Hairline
						detectDragGestures(
							onDragStart = {
								isDragging = true
								contentWidth = currentContentWidth
							},
							onDragEnd = {
								isDragging = false
							},
							onDragCancel = {
								isDragging = false
							},
							onDrag = { change, dragAmount ->
								contentWidth += dragAmount.x.toDp()
								val newContentWidth = contentWidth.coerceIn(contentWidthRange.start, min(width - expendedMinWidth - 1.dp, contentWidthRange.endInclusive))
								onContentWidthChange(newContentWidth)
							}
						)
					}
					.padding(horizontal = paddingHorizontal)
					.background(color = lineColor)
			)
		}
	}
}

@Stable
internal expect fun Modifier.pointerResizeHorizontalHoverIcon(): Modifier

object NoSplitLayoutDefaults {
	
	val ContentWidthRange = 175.dp..400.dp
	
	val ExpendedMinWidth = 350.dp
}