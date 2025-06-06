package com.nocircle.app.pages.guide

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.nocircle.app.pages.account.login.LoginRoute
import com.nocircle.app.pages.main.MainRoute
import com.nocircle.common.navigation.LocalNavController
import com.nocircle.common.navigation.NoPopUp
import com.nocircle.common.navigation.NoRoute
import com.nocircle.compose.material3.NoScaffold
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object GuideRoute : NoRoute

@Composable
fun GuidePage() {
	val viewModel = koinViewModel<GuideViewModel>()
	var verify by remember { mutableStateOf<Boolean?>(null) }
	var already by remember { mutableStateOf(false) }
	var delayFinish by remember { mutableStateOf(false) }
	val controller = LocalNavController.current
	val toNextPage = suspend {
		if (!already && verify != null && delayFinish) {
			already = true
			controller.navigate(
				route = if (verify!!) MainRoute else LoginRoute,
				popup = NoPopUp.Current
			)
		}
	}
	LaunchedEffect(Unit) {
		delay(2000)
		delayFinish = true
		toNextPage()
	}
	LaunchedEffect(Unit) {
		verify = viewModel.verifyToken()
		toNextPage()
	}
	NoScaffold {
		Box(
			modifier = Modifier
				.fillMaxSize()
		) {
			Box(
				modifier = Modifier
					.align(Alignment.Center)
					.size(100.dp)
			) {
				Circle(
					color = Color(0xFF3DDC97),
					initStatus = 0
				)
				Circle(
					color = Color(0xFFFFB74D),
					initStatus = 1
				)
				Circle(
					color = Color(0xFFFF6B6B),
					initStatus = 2
				)
				Circle(
					color = Color(0xFF7E57C2),
					initStatus = 3
				)
			}
			LinearProgressIndicator(
				progress = {
					1f
				},
				modifier = Modifier
					.fillMaxWidth()
					.align(Alignment.BottomCenter)
			)
		}
	}
}

@Composable
private fun Circle(
	color: Color,
	initStatus: Int
) {
	var offset by remember { mutableStateOf(getOffset(initStatus)) }
	var status by remember { mutableIntStateOf(initStatus) }
	LaunchedEffect(Unit) {
		while (true) {
			delay(500)
			status++
			offset = getOffset(status)
		}
	}
	val x by animateDpAsState(
		targetValue = offset.x,
		animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow)
	)
	val y by animateDpAsState(
		targetValue = offset.y,
		animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow)
	)
	Box(
		modifier = Modifier
			.offset(x = x, y = y)
			.size(20.dp)
			.clip(CircleShape)
			.background(color)
	)
}

private fun getOffset(status: Int): DpOffset = when (status % 4) {
	0 -> DpOffset(Dp.Hairline, Dp.Hairline)
	1 -> DpOffset(Dp.Hairline, 80.dp)
	2 -> DpOffset(80.dp, 80.dp)
	else -> DpOffset(80.dp, Dp.Hairline)
}