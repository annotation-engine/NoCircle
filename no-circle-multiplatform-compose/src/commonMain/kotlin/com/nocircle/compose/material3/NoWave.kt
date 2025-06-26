package com.nocircle.compose.material3

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode

@Composable
fun NoWave(
	waveCount: Int,
	modifier: Modifier = Modifier,
	color: Color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
	durationMillis: Int = 1000,
	direction: NoWaveDirection = NoWaveDirection.TO_RIGHT_TOP,
	tileMode: TileMode = TileMode.Clamp,
) {
	require(waveCount > 0) { "waveCount must be > 0" }
	val animate = remember { Animatable(0f) }
	LaunchedEffect(Unit) {
		while (true) {
			animate.snapTo(0f)
			animate.animateTo(1f, animationSpec = tween(durationMillis, easing = LinearEasing))
		}
	}
	val interval by remember(waveCount) {
		derivedStateOf { 1f / (waveCount * 2 + 1) }
	}
	val brush by remember(interval, color, direction, waveCount, animate.value) {
		derivedStateOf {
			val colorStops = (-2..waveCount * 2 + 1).map {
				val stops = it * interval + (interval * 2 * animate.value)
				val color = if (it % 2 == 0) Color.Transparent else color
				stops to color
			}.toTypedArray()
			Brush.linearGradient(
				colorStops = colorStops,
				start = direction.start,
				end = direction.end,
				tileMode = tileMode,
			)
		}
	}
	Box(
		modifier = modifier
			.fillMaxSize()
			.background(brush)
	)
}

private val LeftTop = Offset.Zero
private val LeftBottom = Offset(0f, Float.POSITIVE_INFINITY)
private val RightTop = Offset(Float.POSITIVE_INFINITY, 0f)
private val RightBottom = Offset.Infinite

enum class NoWaveDirection(
	val start: Offset,
	val end: Offset
) {
	TO_LEFT(RightTop, LeftTop),
	TO_RIGHT(LeftTop, RightTop),
	TO_UP(LeftBottom, LeftTop),
	TO_DOWN(LeftTop, LeftBottom),
	TO_LEFT_TOP(RightBottom, LeftTop),
	TO_LEFT_BOTTOM(RightTop, LeftBottom),
	TO_RIGHT_TOP(LeftBottom, RightTop),
	TO_RIGHT_BOTTOM(LeftTop, RightBottom),
}