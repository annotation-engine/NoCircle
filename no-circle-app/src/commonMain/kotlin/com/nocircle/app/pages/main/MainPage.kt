package com.nocircle.app.pages.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Message
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.nocircle.app.generated.resources.*
import com.nocircle.app.pages.main.home.HomePage
import com.nocircle.app.pages.main.message.MessagePage
import com.nocircle.app.pages.main.person.PersonPage
import com.nocircle.common.expends.WindowWidthSizes
import com.nocircle.common.expends.value
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoSnackbarHost
import com.nocircle.compose.material3.showNoSnackbar
import org.jetbrains.compose.resources.StringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainPage() {
	val viewModel = koinViewModel<MainViewModel>()
	val hostState = remember { SnackbarHostState() }
	LaunchedEffect(Unit) {
		hostState.showNoSnackbar(Res.string.login_success)
		viewModel.snackbarCollect(hostState::showNoSnackbar)
	}
	NoScaffold(
		snackbarHost = { NoSnackbarHost(hostState) }
	) { paddingValues ->
		Box(
			modifier = Modifier
				.padding(paddingValues)
				.fillMaxSize()
		) {
			var mainRoute by remember { mutableStateOf(MainRoute.Home) }
			if (WindowWidthSizes.isCompact) {
				CompactMainPage(
					mainRoute = mainRoute,
					onMainRouteChange = { mainRoute = it },
				)
			} else {
				MediumMainPage(
					mainRoute = mainRoute,
					onMainRouteChange = { mainRoute = it },
				)
			}
		}
	}
}

private enum class MainRoute(
	val title: StringResource,
	val icon: ImageVector,
) {
	
	Home(
		title = Res.string.main_home,
		icon = Icons.Rounded.Home,
	),
	
	Message(
		title = Res.string.main_message,
		icon = Icons.AutoMirrored.Rounded.Message
	),
	
	Person(
		title = Res.string.main_person,
		icon = Icons.Rounded.Person
	)
}

@Composable
private fun CompactMainPage(
	mainRoute: MainRoute,
	onMainRouteChange: (MainRoute) -> Unit
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.weight(1f),
			contentAlignment = Alignment.TopCenter
		) {
			MainRoute(mainRoute)
		}
		BottomBar(
			mainRoute = mainRoute,
			onMainRouteChange = onMainRouteChange
		)
	}
}

private val HorizontalItemSpacing = 12.dp

@Composable
private fun BottomBar(
	mainRoute: MainRoute,
	onMainRouteChange: (MainRoute) -> Unit
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(12.dp)
			.height(48.dp)
	) {
		val density = LocalDensity.current
		var width by remember { mutableStateOf(Dp.Unspecified) }
		if (width != Dp.Unspecified) {
			val sliderWidth by remember(width) {
				derivedStateOf {
					val size = MainRoute.entries.size
					(width - HorizontalItemSpacing * (size - 1)) / size
				}
			}
			val offsetXTarget by remember(sliderWidth, mainRoute) {
				derivedStateOf {
					(sliderWidth + HorizontalItemSpacing) * MainRoute.entries.indexOf(mainRoute)
				}
			}
			val offsetX by animateDpAsState(offsetXTarget)
			Box(
				modifier = Modifier
					.offset(x = offsetX)
					.width(sliderWidth)
					.fillMaxHeight()
					.clip(MaterialTheme.shapes.small)
					.background(MaterialTheme.colorScheme.primary)
			)
		}
		Row(
			modifier = Modifier
				.fillMaxSize()
				.onGloballyPositioned {
					width = with(density) { it.size.width.toDp() }
				}
		) {
			MainRoute.entries.fastForEachIndexed { index, it ->
				val color by animateColorAsState(
					targetValue = if (it == mainRoute) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.secondary
				)
				Row(
					modifier = Modifier
						.weight(1f)
						.fillMaxHeight()
						.clip(MaterialTheme.shapes.small)
						.clickable { onMainRouteChange(it) },
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.Center
				) {
					NoIcon(
						icon = it.icon,
						tint = color,
						modifier = Modifier
							.size(24.dp)
					)
					Spacer(modifier = Modifier.width(8.dp))
					Text(
						text = it.title.value,
						color = color,
						style = MaterialTheme.typography.titleSmall,
					)
				}
				if (index < MainRoute.entries.lastIndex) {
					Spacer(modifier = Modifier.width(HorizontalItemSpacing))
				}
			}
		}
	}
}

@Composable
private fun MediumMainPage(
	mainRoute: MainRoute,
	onMainRouteChange: (MainRoute) -> Unit
) {
	Row(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.surfaceContainer),
		verticalAlignment = Alignment.CenterVertically,
	) {
		LeftCenterBar(
			mainRoute = mainRoute,
			onMainRouteChange = onMainRouteChange
		)
		Box(
			modifier = Modifier
				.weight(1f)
				.fillMaxHeight()
				.background(MaterialTheme.colorScheme.surface),
			contentAlignment = Alignment.TopCenter
		) {
			MainRoute(mainRoute)
		}
	}
}

private val VerticalItemSpacing = 8.dp

@Composable
private fun LeftCenterBar(
	mainRoute: MainRoute,
	onMainRouteChange: (MainRoute) -> Unit
) {
	Box(
		modifier = Modifier
			.padding(horizontal = 8.dp)
			.width(56.dp)
	) {
		var height by remember { mutableStateOf(Dp.Unspecified) }
		if (height != Dp.Unspecified) {
			val sliderHeight by remember(height) {
				derivedStateOf {
					val size = MainRoute.entries.size
					(height - VerticalItemSpacing * (size - 1)) / size
				}
			}
			val offsetYTarget by remember(sliderHeight, mainRoute) {
				derivedStateOf {
					(sliderHeight + VerticalItemSpacing) * MainRoute.entries.indexOf(mainRoute)
				}
			}
			val offsetY by animateDpAsState(offsetYTarget)
			Box(
				modifier = Modifier
					.offset(y = offsetY)
					.fillMaxWidth()
					.height(sliderHeight)
					.clip(MaterialTheme.shapes.small)
					.background(MaterialTheme.colorScheme.primary)
			)
		}
		val density = LocalDensity.current
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.onGloballyPositioned {
					height = with(density) { it.size.height.toDp() }
				}
		) {
			MainRoute.entries.fastForEachIndexed { index, it ->
				val color by animateColorAsState(
					targetValue = if (it == mainRoute) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.secondary
				)
				Column(
					modifier = Modifier
						.fillMaxWidth()
						.clip(MaterialTheme.shapes.small)
						.clickable { onMainRouteChange(it) }
						.padding(vertical = 6.dp),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					NoIcon(
						icon = it.icon,
						tint = color,
						modifier = Modifier
							.size(24.dp)
					)
					Spacer(modifier = Modifier.height(2.dp))
					Text(
						text = it.title.value,
						color = color,
						style = MaterialTheme.typography.titleSmall,
					)
				}
				if (index < MainRoute.entries.lastIndex) {
					Spacer(modifier = Modifier.height(VerticalItemSpacing))
				}
			}
		}
	}
}

@Composable
private fun MainRoute(
	mainRoute: MainRoute,
) {
	when (mainRoute) {
		MainRoute.Home -> HomePage()
		MainRoute.Message -> MessagePage()
		MainRoute.Person -> PersonPage()
	}
}