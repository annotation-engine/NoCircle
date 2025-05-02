package com.nocircle.app.pages.guide

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nocircle.app.NoRoute
import com.nocircle.app.utils.ConfigUtils
import com.nocircle.common.expends.navigate
import com.nocircle.compose.icon.NoIcons
import com.nocircle.compose.icon.NoLogo
import com.nocircle.compose.material3.NoScaffold
import kotlinx.coroutines.delay

@Composable
fun GuidePage(
	navController: NavController
) {
	var offsetYTarget by remember { mutableStateOf(100.dp) }
	var alphaTarget by remember { mutableStateOf(0f) }
	var scaleTarget by remember { mutableStateOf(1f) }
	LaunchedEffect(Unit) {
		offsetYTarget = (-200).dp
		alphaTarget = 1f
		delay(1500)
		scaleTarget = 0.5f
		alphaTarget = 0f
		delay(200)
		val token = ConfigUtils.getValue<String>("token")
		if (token != null) {
			navController.navigate(route = NoRoute.MAIN, finish = true)
		} else {
			navController.navigate(route = NoRoute.ACCOUNT_LOGIN, finish = true)
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
				animationSpec = tween(durationMillis = 1200)
			)
			val alpha by animateFloatAsState(
				targetValue = alphaTarget,
				animationSpec = tween(durationMillis = 1200)
			)
			val scale by animateFloatAsState(
				targetValue = scaleTarget,
				animationSpec = tween(durationMillis = 1000)
			)
			Column(
				modifier = Modifier
					.offset(y = offsetY)
					.alpha(alpha)
					.scale(scale),
				horizontalAlignment = Alignment.CenterHorizontally,
			) {
				Image(
					imageVector = NoIcons.NoLogo,
					contentDescription = null,
					modifier = Modifier
						.size(140.dp)
						.background(MaterialTheme.colorScheme.primary, CircleShape)
						.scale(1.48f)
						.clip(CircleShape),
					colorFilter = ColorFilter.tint(Color.White)
				)
				Spacer(Modifier.height(32.dp))
				Text(
					text = "No Circle",
					color = MaterialTheme.colorScheme.onSurface,
					fontSize = 26.sp,
					fontWeight = FontWeight.Bold,
				)
			}
		}
	}
}