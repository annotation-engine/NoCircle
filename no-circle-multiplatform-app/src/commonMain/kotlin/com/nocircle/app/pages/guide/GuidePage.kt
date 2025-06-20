package com.nocircle.app.pages.guide

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.nocircle.compose.animation.animateDpOffsetAsState
import com.nocircle.compose.expends.offset
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.navigation.LocalNavController
import com.nocircle.compose.navigation.NoRoute
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Serializable
data object GuideRoute : NoRoute

@Composable
fun GuidePage() {
	val viewModel = koinViewModel<GuideViewModel>()
	val controller = LocalNavController.current
	LaunchedEffect(Unit) {
		viewModel.navigateTo.collect {
			controller.navigate(it) {
				popUpTo(GuideRoute) {
					inclusive = true
				}
			}
		}
	}
	NoScaffold { paddingValues ->
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues)
		) {
			Box(
				modifier = Modifier
					.align(Alignment.Center)
					.size(260.dp)
			) {
				colors.forEachIndexed { index, color ->
					Circle(
						color = color,
						initStatus = index
					)
				}
			}
			Text(
				text = "© 2025 NoCircle. All rights reserved.",
				modifier = Modifier
					.align(Alignment.BottomCenter)
					.offset(y = (-16).dp),
				style = MaterialTheme.typography.bodySmall,
				color = MaterialTheme.colorScheme.outline
			)
		}
	}
}

private const val DELAY = 400L

@Composable
private fun Circle(
	color: Color,
	initStatus: Int
) {
	var targetOffset by remember { mutableStateOf(getOffset(initStatus)) }
	var status by remember { mutableIntStateOf(initStatus) }
	LaunchedEffect(Unit) {
		delay((DELAY / offsets.size) * initStatus)
		while (true) {
			status++
			targetOffset = getOffset(status)
			delay(DELAY)
		}
	}
	val offset by animateDpOffsetAsState(
		targetValue = targetOffset,
		animationSpec = tween(
			durationMillis = DELAY.toInt()
		)
	)
	Box(
		modifier = Modifier
			.offset(offset)
			.size(20.dp)
			.clip(CircleShape)
			.background(color)
	)
}

private val colors = arrayOf(Color(0xFF3DDC97), Color(0xFFFFB74D), Color(0xFFFF6B6B), Color(0xFF7E57C2), Color(0xFF42A5F5))

private fun getOffset(status: Int): DpOffset = offsets[status % offsets.size]

private val offsets by lazy {
	val c = 120f
	val sin54 = sin(54f.toRadians())
	val r = c / (1 + sin54)
	val angles = arrayOf(-90f, 54f, 198f, -18f, 126f)
	angles.map { angle ->
		val rad = angle.toRadians()
		val x = c + r * cos(rad)
		val y = c + r * sin(rad)
		DpOffset(
			x = x.dp,
			y = y.dp,
		)
	}
}

private fun Float.toRadians(): Float {
	return (this * PI / 180f).toFloat()
}