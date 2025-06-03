package com.nocircle.compose.desktop

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalFoundationApi::class)
@Composable
actual fun NoTooltipArea(
    tooltip: @Composable (() -> Unit),
    modifier: Modifier,
    delayMillis: Int,
    tooltipPlacement: NoTooltipPlacement,
    content: @Composable (() -> Unit)
) {
    TooltipArea(
        tooltip = tooltip,
        modifier = modifier,
        delayMillis = delayMillis,
        tooltipPlacement = tooltipPlacement.toTooltipPlacement(),
        content = content
    )
}

@Stable
@OptIn(ExperimentalFoundationApi::class)
fun NoTooltipPlacement.toTooltipPlacement(): TooltipPlacement {
    return when (this) {
        is NoTooltipPlacement.ComponentRect -> {
            TooltipPlacement.ComponentRect(
                anchor = anchor,
                alignment = alignment,
                offset = offset
            )
        }

        is NoTooltipPlacement.CursorPoint -> {
            TooltipPlacement.CursorPoint(
                offset = offset,
                alignment = alignment,
                windowMargin = windowMargin
            )
        }
    }
}