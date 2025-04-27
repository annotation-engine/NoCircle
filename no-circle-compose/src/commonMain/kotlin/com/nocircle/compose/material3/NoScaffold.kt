package com.nocircle.compose.material3

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

@Composable
fun NoScaffold(
	modifier: Modifier = Modifier,
	topBar: @Composable () -> Unit = {},
	bottomBar: @Composable () -> Unit = {},
	snackbarHost: @Composable () -> Unit = {},
	floatingActionButton: @Composable () -> Unit = {},
	floatingActionButtonPosition: FabPosition = FabPosition.End,
	containerColor: Color = MaterialTheme.colorScheme.background,
	contentColor: Color = contentColorFor(containerColor),
	contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
	backgroundColors: List<Color> = NoScaffoldDefaults.DefaultBackgroundColors,
	content: @Composable (PaddingValues) -> Unit
) {
	Scaffold(
		modifier = modifier,
		topBar = topBar,
		bottomBar = bottomBar,
		snackbarHost = snackbarHost,
		floatingActionButton = floatingActionButton,
		floatingActionButtonPosition = floatingActionButtonPosition,
		contentColor = contentColor,
		contentWindowInsets = contentWindowInsets
	) { paddingValues ->
		BoxWithConstraints {
			val infiniteTransition = rememberInfiniteTransition(label = "NoInfiniteTransition")
			val angle by infiniteTransition.animateFloat(
				initialValue = 0f,
				targetValue = 360f,
				animationSpec = infiniteRepeatable(
					animation = tween(durationMillis = 4000, easing = LinearEasing)
				),
				label = "NoFloatAnimation"
			)
			val width = with(LocalDensity.current) { maxWidth.toPx() }
			val height = with(LocalDensity.current) { maxWidth.toPx() }
			val center = Offset(width / 2f, height / 2f)
			val radius = hypot(width / 2f, height / 2f)
			val radian = angle * PI / 180f
			val dx = cos(radian).toFloat() * radius
			val dy = sin(radian).toFloat() * radius
			val offset = Offset(dx, dy)
			val start = center - offset
			val end = center + offset
			Box(
				modifier = Modifier
					.fillMaxSize()
					.background(
						brush = Brush.linearGradient(
							colors = backgroundColors,
							start = start,
							end = end
						)
					)
			) {
				content(paddingValues)
			}
		}
	}
}

object NoScaffoldDefaults {
	
	val DefaultBackgroundColors: List<Color>
		@Composable get() = listOf(
			MaterialTheme.colorScheme.primaryContainer.copy(0.4f),
			MaterialTheme.colorScheme.tertiaryContainer.copy(0.4f)
		)
}