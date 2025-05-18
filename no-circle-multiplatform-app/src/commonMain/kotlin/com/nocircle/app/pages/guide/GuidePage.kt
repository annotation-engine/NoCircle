package com.nocircle.app.pages.guide

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.nocircle.app.NoNavControllerManagers
import com.nocircle.app.NoRoutes
import com.nocircle.compose.icon.NoIcons
import com.nocircle.compose.icon.NoLogo
import com.nocircle.compose.material3.NoScaffold
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GuidePage() {
	val viewModel = koinViewModel<GuideViewModel>()
	var offsetYTarget by remember { mutableStateOf(50.dp) }
	var alphaTarget by remember { mutableStateOf(0f) }
	var scaleTarget by remember { mutableStateOf(1f) }
	var alphaSpec by remember { mutableStateOf(tween<Float>(durationMillis = 1000)) }
	val navController = NoNavControllerManagers.get()
	LaunchedEffect(Unit) {
		delay(300)
		offsetYTarget = (-100).dp
		alphaTarget = 1f
		delay(1100)
		alphaSpec = tween(durationMillis = 250)
		scaleTarget = 0.8f
		alphaTarget = 0f
		delay(250)
		if (viewModel.verifyToken()) {
			navController.navigate(route = NoRoutes.Main, finish = true)
		} else {
			navController.navigate(route = NoRoutes.Login, finish = true)
		}
	}
	NoScaffold {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(it),
			contentAlignment = Alignment.Center
		) {
			val offsetY by animateDpAsState(
				targetValue = offsetYTarget,
				animationSpec = tween(durationMillis = 1000)
			)
			val alpha by animateFloatAsState(
				targetValue = alphaTarget,
				animationSpec = alphaSpec
			)
			val scale by animateFloatAsState(
				targetValue = scaleTarget,
				animationSpec = tween(durationMillis = 400)
			)
			Image(
				imageVector = NoIcons.NoLogo,
				contentDescription = null,
				modifier = Modifier
					.offset(y = offsetY)
					.alpha(alpha)
					.scale(scale)
					.size(140.dp)
					.background(MaterialTheme.colorScheme.primary, CircleShape)
					.scale(1.5f)
					.clip(CircleShape),
				colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
			)
		}
	}
}