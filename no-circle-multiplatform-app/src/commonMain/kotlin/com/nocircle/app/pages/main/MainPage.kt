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
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.nocircle.app.generated.resources.*
import com.nocircle.app.pages.main.friends.FriendsPage
import com.nocircle.app.pages.main.groups.GroupsPage
import com.nocircle.app.pages.main.home.HomePage
import com.nocircle.app.pages.main.person.PersonPage
import com.nocircle.app.pages.settings.SettingsPage
import com.nocircle.app.pages.settings.SettingsRoute
import com.nocircle.app.pages.settings.appearance.AppearancePage
import com.nocircle.app.pages.settings.appearance.AppearanceRoute
import com.nocircle.app.pages.settings.appearance.AppearanceViewModel
import com.nocircle.app.theme.colors.ThemeMode
import com.nocircle.common.navigation.*
import com.nocircle.common.windowsize.WindowWidthSizes
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoSnackbarHost
import com.nocircle.compose.material3.showNoSnackbar
import com.nocircle.compose.resources.value
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object MainRoute : NoRoute

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
	) {
		Row(
			modifier = Modifier
				.fillMaxSize()
		) {
			val isCompat = WindowWidthSizes.isCompact
			val subRoute by viewModel.mainSubRoute.collectAsState()
			LocalNavControllerProvider { controller ->
				if (!isCompat) {
					LeftNavigationBar(
						subRoute = subRoute,
						onSubRouteChange = {
							if (subRoute != it) {
								viewModel.mainSubRoute.value = it
							}
							if (controller.currentRoute != MainRoute::class) {
								controller.navigate(route = MainRoute, popup = NoPopUp.All)
							}
						}
					)
				}
				NoNavHost(
					navController = controller,
					startDestination = MainRoute,
					modifier = Modifier
						.weight(1f)
						.fillMaxHeight(),
					navTransition = if (isCompat) NavTransition.HorizontalSlide else NavTransition.Fade
				) {
					composable<MainRoute>(
						navTransition = NavTransition.Fade,
						content = {
							MainPage(
								subRoute = subRoute,
								onSubRouteChange = {
									if (subRoute != it) {
										viewModel.mainSubRoute.value = it
									}
									if (controller.currentRoute != MainRoute::class) {
										controller.navigate(route = MainRoute, popup = NoPopUp.All)
									}
								}
							)
						}
					)
					composable<SettingsRoute> { SettingsPage() }
					composable<AppearanceRoute> { AppearancePage() }
				}
			}
		}
	}
}

@Composable
private fun MainPage(
	subRoute: MainSubRoute,
	onSubRouteChange: (MainSubRoute) -> Unit
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
			if (WindowWidthSizes.isCompact) {
				BottomNavigationBar(
					subRoute = subRoute,
					onSubRouteChange = onSubRouteChange
				)
			}
		}
	}
}

private val HorizontalItemSpacing = 12.dp

@Composable
private fun BottomNavigationBar(
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

@Composable
private fun LeftNavigationBar(
	subRoute: MainSubRoute,
	onSubRouteChange: (MainSubRoute) -> Unit
) {
	val viewModel = koinViewModel<MainViewModel>()
	val isLeftNavigationBarExpended by viewModel.isLeftNavigationBarExpended.collectAsState()
	Column(
		modifier = Modifier
			.width(if (isLeftNavigationBarExpended) 140.dp else 60.dp)
			.fillMaxHeight()
			.background(MaterialTheme.colorScheme.surfaceContainer)
			.padding(
				top = 32.dp,
				start = 6.dp,
				end = 6.dp,
				bottom = 6.dp
			)
	) {
		val controller = LocalNavController.current
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.height(48.dp)
				.clip(MaterialTheme.shapes.small)
				.clickable {
					controller.popBackStack()
				}
				.padding(12.dp),
			verticalAlignment = Alignment.CenterVertically,
		) {
			NoIcon(
				icon = Icons.AutoMirrored.Rounded.ArrowBackIos,
				modifier = Modifier.size(24.dp),
				tint = MaterialTheme.colorScheme.primary
			)
			if (isLeftNavigationBarExpended) {
				Spacer(modifier = Modifier.width(8.dp))
				Text(
					text = "上一页",
					color = MaterialTheme.colorScheme.primary,
					style = MaterialTheme.typography.bodyMedium
				)
			}
		}
		Spacer(modifier = Modifier.height(6.dp))
		Spacer(modifier = Modifier.weight(1f))
		MainSubRoute.entries.forEachIndexed { index, route ->
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.height(48.dp)
					.clip(MaterialTheme.shapes.small)
					.background(
						color = if (subRoute == route) MaterialTheme.colorScheme.primary else Color.Transparent,
						shape = MaterialTheme.shapes.small
					)
					.clickable { onSubRouteChange(route) }
					.padding(12.dp),
				verticalAlignment = Alignment.CenterVertically,
			) {
				NoIcon(
					icon = route.icon,
					modifier = Modifier.size(24.dp),
					tint = if (subRoute == route) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
				)
				if (isLeftNavigationBarExpended) {
					Spacer(modifier = Modifier.width(12.dp))
					Text(
						text = route.title.value(),
						color = if (subRoute == route) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
						style = MaterialTheme.typography.bodyMedium
					)
				}
			}
			if (index < MainSubRoute.entries.lastIndex) {
				Spacer(modifier = Modifier.height(4.dp))
			}
		}
		Spacer(modifier = Modifier.weight(1f))
		Spacer(modifier = Modifier.height(6.dp))
		val appearanceViewModel = koinViewModel<AppearanceViewModel>()
		val attribute by appearanceViewModel.colorSchemeAttribute.collectAsState()
		val isDark = attribute.themeMode.isDark
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.height(48.dp)
				.clip(MaterialTheme.shapes.small)
				.clickable {
					appearanceViewModel.colorSchemeAttribute.value = attribute.copy(
						themeMode = if (isDark) ThemeMode.Light else ThemeMode.Dark
					)
				}
				.padding(12.dp),
			verticalAlignment = Alignment.CenterVertically,
		) {
			NoIcon(
				icon = if (isDark) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
				modifier = Modifier.size(24.dp),
				tint = MaterialTheme.colorScheme.primary
			)
			if (isLeftNavigationBarExpended) {
				Spacer(modifier = Modifier.width(8.dp))
				Text(
					text = if (isDark) Res.string.appearance_theme_mode_light.value() else Res.string.appearance_theme_mode_dark.value(),
					color = MaterialTheme.colorScheme.primary,
					style = MaterialTheme.typography.bodyMedium,
				)
			}
		}
		Spacer(modifier = Modifier.width(6.dp))
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.height(48.dp)
				.clip(MaterialTheme.shapes.small)
				.clickable {
					viewModel.isLeftNavigationBarExpended.value = !isLeftNavigationBarExpended
				}
				.padding(12.dp),
			verticalAlignment = Alignment.CenterVertically,
		) {
			NoIcon(
				icon = if (isLeftNavigationBarExpended) Icons.Rounded.KeyboardDoubleArrowLeft else Icons.Rounded.KeyboardDoubleArrowRight,
				modifier = Modifier.size(24.dp),
				tint = MaterialTheme.colorScheme.primary
			)
			if (isLeftNavigationBarExpended) {
				Spacer(modifier = Modifier.width(8.dp))
				Text(
					text = "收起",
					color = MaterialTheme.colorScheme.primary,
					style = MaterialTheme.typography.bodyMedium,
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