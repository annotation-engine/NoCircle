package com.nocircle.app.pages.main.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.nocircle.app.pages.main.MainSubRoute
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.resources.value
import com.nocircle.compose.windowsize.WindowWidthSizes
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun BottomNavigationBar(
	subRoute: MainSubRoute,
	onSubRouteChange: (MainSubRoute) -> Unit
) {
	val targetHeight by _bottomNavigationBarHeight.collectAsState()
	val height by animateDpAsState(targetHeight)
	val showDivider by remember(height) {
		derivedStateOf { height > Dp.Hairline }
	}
	if (showDivider) {
		HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
	}
	Row(
		modifier = Modifier
			.background(MaterialTheme.colorScheme.surfaceContainerLow)
			.height(height)
			.padding(16.dp)
	) {
		MainSubRoute.entries.fastForEachIndexed { index, current ->
			Column(
				modifier = Modifier
					.weight(1f)
					.fillMaxHeight()
					.clip(MaterialTheme.shapes.medium)
					.clickable {
						onSubRouteChange(current)
					}
					.padding(6.dp),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				val selected = subRoute == current
				val color by animateColorAsState(
					targetValue = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
				)
				val iconSize by animateDpAsState(
					targetValue = if (selected) 44.dp else 24.dp
				)
				NoIcon(
					icon = current.icon.value(),
					modifier = Modifier.size(iconSize),
					tint = color
				)
				Spacer(modifier = Modifier.height(4.dp))
				Text(
					text = current.title.value(),
					modifier = Modifier.height(16.dp),
					style = MaterialTheme.typography.bodySmall,
					color = color
				)
			}
		}
	}
}

private val _bottomNavigationBarHeight = MutableStateFlow(88.dp)

@Composable
fun AutoVisibleBottomNavigation() {
	val isCompact = WindowWidthSizes.isCompact
	DisposableEffect(Unit) {
		if (isCompact) {
			_bottomNavigationBarHeight.value = Dp.Hairline
		}
		onDispose {
			_bottomNavigationBarHeight.value = 88.dp
		}
	}
}