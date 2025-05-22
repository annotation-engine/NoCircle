package com.nocircle.app.pages.main

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Person2
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
import com.nocircle.app.NavControllerKey
import com.nocircle.app.NavRoot
import com.nocircle.app.NoNavControllerManager
import com.nocircle.app.generated.resources.*
import com.nocircle.app.pages.main.friends.FriendsPage
import com.nocircle.app.pages.main.groups.GroupsPage
import com.nocircle.app.pages.main.home.HomePage
import com.nocircle.app.pages.main.person.PersonPage
import com.nocircle.app.pages.settings.SettingsPage
import com.nocircle.app.pages.settings.SettingsRoute
import com.nocircle.app.pages.settings.appearance.AppearancePage
import com.nocircle.app.pages.settings.appearance.AppearanceRoute
import com.nocircle.common.navigation.*
import com.nocircle.common.windowsize.WindowWidthSizes
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoSnackbarHost
import com.nocircle.compose.material3.showNoSnackbar
import com.nocircle.compose.resources.value
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object MainRoute : NoRoute

data object NavMain : NavControllerKey

@Composable
fun MainPage() {
	val viewModel = koinViewModel<MainViewModel>()
	val hostState = remember { SnackbarHostState() }
	val controller = NoNavControllerManager[NavRoot]
	LaunchedEffect(Unit) {
		if (controller.resultRoute == null) {
			hostState.showNoSnackbar(Res.string.login_success)
		}
		viewModel.snackbarCollect(hostState::showNoSnackbar)
	}
	NoScaffold(
		snackbarHost = { NoSnackbarHost(hostState) }
	) {
		Row(
			modifier = Modifier
				.fillMaxSize()
		) {
			val controller = NoNavControllerManager[NavMain]
			val isCompat = WindowWidthSizes.isCompact
			val subRoute by viewModel.mainSubRoute.collectAsState()
			if (!isCompat) {
				LeftBar(
					subRoute = subRoute,
					onSubRouteChange = {
						viewModel.mainSubRoute.value = it
						controller.navigate(MainRoute, popup = NoPopUp.All)
					}
				)
			}
			Column(
				modifier = Modifier
					.weight(1f)
					.fillMaxHeight()
			) {
				val navController = NoNavControllerManager[NavMain]
				NoNavHost(
					navController = navController,
					startDestination = MainRoute,
					navTransition = if (isCompat) HorizontalSlideTransition else FadeTransition
				) {
					composable<MainRoute> { MainPage(subRoute) }
					composable<SettingsRoute> { SettingsPage() }
					composable<AppearanceRoute> { AppearancePage() }
				}
			}
		}
	}
}

private val HorizontalItemSpacing = 12.dp

@Composable
private fun BottomBar(
	subRoute: MainSubRoute,
	onSubRouteChange: (MainSubRoute) -> Unit
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
					val size = MainSubRoute.entries.size
					(width - HorizontalItemSpacing * (size - 1)) / size
				}
			}
			val offsetXTarget by remember(sliderWidth, subRoute) {
				derivedStateOf {
					(sliderWidth + HorizontalItemSpacing) * MainSubRoute.entries.indexOfFirst { it == subRoute }
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
			MainSubRoute.entries.fastForEachIndexed { index, it ->
				val color by animateColorAsState(
					targetValue = if (it == subRoute) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.secondary
				)
				Row(
					modifier = Modifier
						.weight(1f)
						.fillMaxHeight()
						.clip(MaterialTheme.shapes.small)
						.clickable { onSubRouteChange(it) },
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.Center
				) {
					NoIcon(
						icon = it.icon,
						tint = color,
						modifier = Modifier
							.size(22.dp)
					)
					Spacer(modifier = Modifier.width(8.dp))
					Text(
						text = it.title.value(),
						color = color,
						style = MaterialTheme.typography.bodySmall,
					)
				}
				if (index < MainSubRoute.entries.lastIndex) {
					Spacer(modifier = Modifier.width(HorizontalItemSpacing))
				}
			}
		}
	}
}

private val VerticalItemSpacing = 12.dp

@Composable
private fun LeftBar(
	subRoute: MainSubRoute,
	onSubRouteChange: (MainSubRoute) -> Unit
) {
	Box(
		modifier = Modifier
			.width(68.dp)
			.fillMaxHeight()
			.background(MaterialTheme.colorScheme.surfaceContainerHigh)
			.padding(
				horizontal = 6.dp,
				vertical = 32.dp
			)
	) {
		val navController = NoNavControllerManager[NavMain]
		NoIconButton(
			icon = Icons.AutoMirrored.Rounded.ArrowBackIos,
			modifier = Modifier
				.size(44.dp)
				.align(Alignment.TopCenter),
			tint = MaterialTheme.colorScheme.primary,
			paddingValues = PaddingValues(10.dp),
		) {
			navController.popBackStack()
		}
		
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.align(Alignment.Center)
		) {
			var height by remember { mutableStateOf(Dp.Unspecified) }
			if (height != Dp.Unspecified) {
				val sliderHeight by remember(height) {
					derivedStateOf {
						val size = MainSubRoute.entries.size
						(height - VerticalItemSpacing * (size - 1)) / size
					}
				}
				val offsetYTarget by remember(sliderHeight, subRoute) {
					derivedStateOf {
						(sliderHeight + VerticalItemSpacing) * MainSubRoute.entries.indexOfFirst { it == subRoute }
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
				MainSubRoute.entries.fastForEachIndexed { index, it ->
					val color by animateColorAsState(
						targetValue = if (it == subRoute) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.secondary
					)
					Column(
						modifier = Modifier
							.fillMaxWidth()
							.height(56.dp)
							.clip(MaterialTheme.shapes.small)
							.clickable { onSubRouteChange(it) },
						verticalArrangement = Arrangement.Center,
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						NoIcon(
							icon = it.icon,
							tint = color,
							modifier = Modifier
								.size(22.dp)
						)
						Spacer(modifier = Modifier.height(2.dp))
						Text(
							text = it.title.value(),
							color = color,
							style = MaterialTheme.typography.bodySmall,
						)
					}
					if (index < MainSubRoute.entries.size - 1) {
						Spacer(modifier = Modifier.height(VerticalItemSpacing))
					}
				}
			}
		}
	}
}

@Composable
private fun MainPage(
	subRoute: MainSubRoute
) {
	NoScaffold { paddingValues ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues)
		) {
			Crossfade(
				targetState = subRoute,
				modifier = Modifier
					.fillMaxWidth()
					.weight(1f),
				animationSpec = tween(durationMillis = 120),
				label = "CompactMainRouteCrossfade",
			) { target ->
				when (target) {
					MainSubRoute.Home -> HomePage()
					MainSubRoute.Friends -> FriendsPage()
					MainSubRoute.Groups -> GroupsPage()
					MainSubRoute.Person -> PersonPage()
				}
			}
			
			val isCompat = WindowWidthSizes.isCompact
			val viewModel = koinViewModel<MainViewModel>()
			if (isCompat) {
				val controller = NoNavControllerManager[NavMain]
				BottomBar(
					subRoute = subRoute,
					onSubRouteChange = {
						viewModel.mainSubRoute.value = it
						controller.navigate(MainRoute, popup = NoPopUp.All)
					}
				)
			}
		}
	}
}

enum class MainSubRoute(
	val title: StringResource,
	val icon: ImageVector
) {
	Home(
		title = Res.string.main_home,
		icon = Icons.Rounded.Home
	),
	Friends(
		title = Res.string.main_friends,
		icon = Icons.Rounded.Person2
	),
	Groups(
		title = Res.string.main_groups,
		icon = Icons.Rounded.Group
	),
	Person(
		title = Res.string.main_person,
		icon = Icons.Rounded.Person
	)
}