package com.nocircle.compose.layout

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.nocircle.compose.expends.toDpSize
import com.nocircle.compose.windowsize.WindowWidthSizes

@Composable
fun NoSplitLayout(
	contentWidth: Dp,
	onContentWidthChange: (Dp) -> Unit,
	expended: @Composable () -> Unit,
	modifier: Modifier = Modifier,
	contentWidthRange: ClosedRange<Dp> = NoSplitLayoutDefaults.ContentWidthRange,
	expendedMinWidth: Dp = NoSplitLayoutDefaults.ExpendedMinWidth,
	content: @Composable BoxScope.(isCompat: Boolean) -> Unit,
) {
	val isCompact = WindowWidthSizes.isCompact
	var size by remember { mutableStateOf(DpSize.Unspecified) }
	val density = LocalDensity.current
	LaunchedEffect(size.width) {
		if (!isCompact) {
			val newContentWidth = size.width - expendedMinWidth - 1.dp
			if (contentWidth > newContentWidth) {
				onContentWidthChange(newContentWidth)
			}
		}
	}
	Box(
		modifier = modifier
			.fillMaxSize()
			.onSizeChanged {
				size = it.toDpSize(density)
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
			var centerPercent by remember { mutableStateOf(0f) }
			var isFocused by remember { mutableStateOf(false) }
			val centerAlpha by animateFloatAsState(
				targetValue = when {
					isFocused -> 1f
					isHovered -> 0.6f
					else -> 0.15f
				}
			)
			val color = MaterialTheme.colorScheme.primary
			val lineBrush by remember(color, centerPercent, centerAlpha) {
				derivedStateOf {
					Brush.verticalGradient(
						0f to color.copy(alpha = 0.15f),
						centerPercent to color.copy(alpha = centerAlpha),
						1f to color.copy(alpha = 0.15f),
					)
				}
			}
			val paddingHorizontal by animateDpAsState(
				targetValue = when {
					isFocused || isHovered -> 7.5.dp
					else -> 8.dp
				}
			)
			Box(
				modifier = Modifier
					.offset(x = contentWidth - 8.dp)
					.width(17.dp)
					.fillMaxHeight()
					.pointerResizeHorizontalHoverIcon()
					.hoverable(interactionSource)
					.pointerInput(size.width, expendedMinWidth, contentWidthRange) {
						var contentWidth = Dp.Hairline
						detectDragGestures(
							onDragStart = {
								isFocused = true
								contentWidth = currentContentWidth
							},
							onDragEnd = {
								isFocused = false
							},
							onDragCancel = {
								isFocused = false
							},
							onDrag = { change, dragAmount ->
								contentWidth += dragAmount.x.toDp()
								val newContentWidth = contentWidth.coerceIn(contentWidthRange.start, min(size.width - expendedMinWidth - 1.dp, contentWidthRange.endInclusive))
								onContentWidthChange(newContentWidth)
							}
						)
					}
					.pointerInput(Unit) {
						detectTapGestures(
							onPress = {
								isFocused = true
							},
							onTap = {
								isFocused = false
							}
						)
					}
					.pointerInput(Unit) {
						awaitPointerEventScope {
							while (true) {
								val event = awaitPointerEvent()
								val y = event.changes.firstOrNull()?.position?.y
								if (y != null) {
									centerPercent = y / size.height.toPx()
								}
							}
						}
					}
					.padding(horizontal = paddingHorizontal)
					.background(lineBrush)
			)
		}
	}
}

@Stable
internal expect fun Modifier.pointerResizeHorizontalHoverIcon(): Modifier

object NoSplitLayoutDefaults {
	
	val ContentWidthRange = 200.dp..300.dp
	
	val ExpendedMinWidth = 327.dp
}