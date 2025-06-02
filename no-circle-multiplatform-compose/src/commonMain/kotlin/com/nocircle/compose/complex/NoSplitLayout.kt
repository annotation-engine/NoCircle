package com.nocircle.compose.complex

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nocircle.common.windowsize.WindowWidthSizes
import com.nocircle.compose.range.DpRange

@Composable
fun NoSplitLayout(
    contentWidth: Dp,
    onContentWidthChange: (Dp) -> Unit,
    contentWidthRange: DpRange = 200.dp..400.dp,
    expend: @Composable () -> Unit,
    content: @Composable (isCompat: Boolean) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val isCompact = WindowWidthSizes.isCompact
        var isDragging by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .then(if (isCompact) Modifier.fillMaxWidth() else Modifier.width(contentWidth))
            ) {
                content(isCompact)
            }
            if (!isCompact) {
                Spacer(modifier = Modifier.width(1.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    expend()
                }
            }
        }
        if (!isCompact) {
            val currentWidth by rememberUpdatedState(contentWidth)
            val interactionSource = remember { MutableInteractionSource() }
            val isHovered by interactionSource.collectIsHoveredAsState()
            var isDragging by remember { mutableStateOf(false) }
            val isHighlight by remember {
                derivedStateOf { isHovered || isDragging }
            }
            val paddingHorizontal by animateDpAsState(
                targetValue = if (isHighlight) 3.5.dp else 4.dp
            )
            val lineColor by animateColorAsState(
                targetValue = if (isHighlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
            )
            val shadowElevation by animateDpAsState(
                targetValue = if (isHighlight) 20.dp else Dp.Hairline
            )
            Box(
                modifier = Modifier
                    .width(9.dp)
                    .fillMaxHeight()
                    .offset(contentWidth - 4.dp)
                    .pointerResizeHorizontalHoverIcon()
                    .hoverable(interactionSource)
                    .pointerInput(Unit) {
                        var width = Dp.Hairline
                        detectDragGestures(
                            onDragStart = {
                                isDragging = true
                                width = currentWidth
                            },
                            onDragEnd = {
                                isDragging = false
                            },
                            onDragCancel = {
                                isDragging = false
                            },
                            onDrag = { _, dragAmount ->
                                width += dragAmount.x.toDp()
                                onContentWidthChange(width.coerceIn(contentWidthRange))
                            }
                        )
                    }
                    .padding(horizontal = paddingHorizontal)
                    .shadow(
                        elevation = shadowElevation,
                        shape = CircleShape,
                        ambientColor = MaterialTheme.colorScheme.primary,
                        spotColor = MaterialTheme.colorScheme.primary
                    )
                    .background(color = lineColor)
            )
        }
    }
}

internal expect fun Modifier.pointerResizeHorizontalHoverIcon(): Modifier