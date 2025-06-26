package com.nocircle.compose.layout

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nocircle.compose.expends.toDpSize
import com.nocircle.compose.navigation.*
import com.nocircle.compose.windowsize.WindowWidthSizes
import kotlinx.coroutines.delay

@Composable
fun <T : Any> NoAdaptiveSplitScreen(
	current: T?,
	onCurrentChange: (T?) -> Unit,
	leftWidth: Dp,
	onLeftWidthChange: (Dp) -> Unit,
	modifier: Modifier = Modifier,
	leftWidthRange: ClosedRange<Dp> = NoAdaptiveSplitScreenDefaults.LeftWidthRange,
	rightMinWidth: Dp = NoAdaptiveSplitScreenDefaults.RightMinWidth,
	leftContent: @Composable (current: T?, navigate: (data: T) -> Unit) -> Unit,
	rightContent: @Composable (data: T?, popBackStack: () -> Unit) -> Unit,
	rightEmptyContent: @Composable () -> Unit
) {
	val isCompact = WindowWidthSizes.isCompact
	val controller = rememberNavController()
	var isHorizontalSlideTransition by remember { mutableStateOf(true) }
	LaunchedEffect(isCompact) {
		if (!isCompact && controller.currentDestination?.route == ADAPTIVE_RIGHT) {
			isHorizontalSlideTransition = false
			controller.popBackStack()
			delay(300L)
			isHorizontalSlideTransition = true
		}
	}
	NavHost(
		navController = controller,
		startDestination = ADAPTIVE_SPLIT_SCREEN,
		modifier = modifier,
		enterTransition = enterTransition { if (isHorizontalSlideTransition) horizontalSlider() else none },
		exitTransition = exitTransition { if (isHorizontalSlideTransition) horizontalSlider() else none },
		popEnterTransition = popEnterTransition { if (isHorizontalSlideTransition) horizontalSlider() else none },
		popExitTransition = popExitTransition { if (isHorizontalSlideTransition) horizontalSlider() else none }
	) {
		composable(
			route = ADAPTIVE_SPLIT_SCREEN
		) {
			var size by remember { mutableStateOf(DpSize.Unspecified) }
			val density = LocalDensity.current
			Box(
				modifier = Modifier
					.fillMaxSize()
					.onSizeChanged {
						size = it.toDpSize(density)
					}
			) {
				Box(
					modifier = Modifier
						.fillMaxHeight()
						.then(if (isCompact) Modifier.fillMaxWidth() else Modifier.width(leftWidth))
				) {
					leftContent(current) {
						onCurrentChange(it)
						if (isCompact) {
							controller.currentSavedStateHandle?.set("data", it)
							controller.navigate(ADAPTIVE_RIGHT) {
								launchSingleTop = true
							}
						}
					}
				}
				if (!isCompact) {
					Box(
						modifier = Modifier
							.padding(start = leftWidth + 1.dp)
							.fillMaxSize()
					) {
						if (current != null) {
							rightContent(current) {
								onCurrentChange(null)
							}
						} else {
							rightEmptyContent()
						}
					}
					HighlightDivider(
						leftWidth = leftWidth,
						onLeftWidthChange = onLeftWidthChange,
						leftWidthRange = leftWidthRange,
						rightMinWidth = rightMinWidth,
						size = size
					)
				}
			}
		}
		composable(
			route = ADAPTIVE_RIGHT
		) {
			val data = controller.previousSavedStateHandle?.get<T>("data")
			rightContent(data) {
				onCurrentChange(null)
				controller.popBackStack()
			}
		}
	}
}

private const val ADAPTIVE_SPLIT_SCREEN = "/adaptive/splitScreen"
private const val ADAPTIVE_RIGHT = "/adaptive/right"

@Composable
private fun HighlightDivider(
	leftWidth: Dp,
	onLeftWidthChange: (Dp) -> Unit,
	leftWidthRange: ClosedRange<Dp>,
	rightMinWidth: Dp,
	size: DpSize
) {
	val interactionSource = remember { MutableInteractionSource() }
	var isFocused by remember { mutableStateOf(false) }
	val isHovered by interactionSource.collectIsHoveredAsState()
	val paddingHorizontal by animateDpAsState(
		targetValue = when {
			isFocused || isHovered -> 7.5.dp
			else -> 8.dp
		}
	)
	val currentLeftWidth by rememberUpdatedState(leftWidth)
	val lineColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
	val primaryColor = MaterialTheme.colorScheme.primary
	val bothEndsColor by animateColorAsState(
		targetValue = when {
			isFocused || isHovered -> primaryColor.copy(alpha = 0.15f)
			else -> lineColor
		}
	)
	val centerColor by animateColorAsState(
		targetValue = when {
			isFocused -> primaryColor
			isHovered -> primaryColor.copy(alpha = 0.6f)
			else -> lineColor
		}
	)
	var centerPercent by remember { mutableStateOf(0.5f) }
	val lineBrush by remember(bothEndsColor, centerColor, centerPercent) {
		derivedStateOf {
			Brush.verticalGradient(
				0f to bothEndsColor,
				centerPercent to centerColor,
				1f to bothEndsColor
			)
		}
	}
	val isCompact = WindowWidthSizes.isCompact
	LaunchedEffect(size.width) {
		if (!isCompact) {
			val newLeftWidth = size.width - rightMinWidth - 1.dp
			if (leftWidth > newLeftWidth) {
				onLeftWidthChange(newLeftWidth)
			}
		}
	}
	Box(
		modifier = Modifier
			.offset(x = leftWidth - 8.dp)
			.width(17.dp)
			.fillMaxHeight()
			.pointerResizeHorizontalHoverIcon()
			.hoverable(interactionSource)
			.pointerInput(size.width, rightMinWidth, leftWidthRange) {
				var leftWidth = Dp.Hairline
				detectDragGestures(
					onDragStart = {
						isFocused = true
						leftWidth = currentLeftWidth
					},
					onDragEnd = {
						isFocused = false
					},
					onDragCancel = {
						isFocused = false
					},
					onDrag = { change, dragAmount ->
						leftWidth += dragAmount.x.toDp()
						val newLeftWidth = leftWidth.coerceIn(leftWidthRange.start, min(size.width - rightMinWidth - 1.dp, leftWidthRange.endInclusive))
						onLeftWidthChange(newLeftWidth)
					}
				)
			}
			.pointerInput(Unit) {
				detectTapGestures(
					onPress = { isFocused = true },
					onTap = { isFocused = false }
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
			.background(
				brush = lineBrush,
				shape = CircleShape
			)
	)
}

@Stable
internal expect fun Modifier.pointerResizeHorizontalHoverIcon(): Modifier

object NoAdaptiveSplitScreenDefaults {
	
	val LeftWidthRange = 200.dp..300.dp
	
	val RightMinWidth = 327.dp
}