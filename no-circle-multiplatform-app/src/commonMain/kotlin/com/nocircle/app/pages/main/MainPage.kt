package com.nocircle.app.pages.main

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.nocircle.app.pages.main.friends.FriendsPage
import com.nocircle.app.pages.main.groups.GroupsPage
import com.nocircle.app.pages.main.home.HomePage
import com.nocircle.app.pages.main.person.PersonPage
import com.nocircle.app.pages.main.person.request.FriendAddRequestPage
import com.nocircle.app.pages.main.person.request.FriendAddRequestRoute
import com.nocircle.app.pages.settings.SettingsPage
import com.nocircle.app.pages.settings.SettingsRoute
import com.nocircle.app.pages.settings.appearance.AppearancePage
import com.nocircle.app.pages.settings.appearance.AppearanceRoute
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.app.theme.colors.ThemeMode
import com.nocircle.common.navigation.*
import com.nocircle.common.resources.getString
import com.nocircle.common.resources.value
import com.nocircle.common.windowsize.WindowWidthSizes
import com.nocircle.compose.desktop.NoTooltipArea
import com.nocircle.compose.desktop.NoTooltipPlacement
import com.nocircle.compose.desktop.NoWindowDraggableArea
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.showNoSnackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object MainRoute : NoRoute

@Composable
fun MainPage() {
	val viewModel = koinViewModel<MainViewModel>()
	val hostState = remember { SnackbarHostState() }
	LaunchedEffect(Unit) {
		hostState.showNoSnackbar(AppString.LoginSuccess.getString())
		viewModel.snackbarCollect(hostState::showNoSnackbar)
	}
	NoScaffold(
		snackbarHostState = hostState,
	) {
		val isCompat = WindowWidthSizes.isCompact
		val subRoute by viewModel.mainSubRoute.collectAsState()
		LocalNavControllerProvider { controller ->
			val onSubRouteChange = { route: MainSubRoute ->
				if (subRoute != route) {
					viewModel.mainSubRoute.value = route
				}
				if (controller.currentRoute != MainRoute::class) {
					controller.navigate(route = MainRoute, popup = NoPopUp.All)
				}
			}
			NoNavHost(
				navController = controller,
				startDestination = MainRoute,
				modifier = Modifier
					.padding(start = if (isCompat) Dp.Hairline else LeftNavigationWidth)
					.background(MaterialTheme.colorScheme.surface)
					.fillMaxSize(),
				navTransition = if (isCompat) NavTransition.HorizontalSlide else NavTransition.Fade,
				navPopTransition = if (isCompat) NavPopTransition.HorizontalSlide else NavPopTransition.Fade,
			) {
				composable<MainRoute> {
					MainPage(
						subRoute = subRoute,
						onSubRouteChange = onSubRouteChange
					)
				}
				composable<SettingsRoute> { SettingsPage() }
				composable<AppearanceRoute> { AppearancePage() }
				composable<FriendAddRequestRoute> { FriendAddRequestPage() }
			}
			if (!isCompat) {
				LeftNavigationBar(
					subRoute = subRoute,
					onSubRouteChange = onSubRouteChange
				)
			}
		}
	}
}

@Composable
private fun MainPage(
	subRoute: MainSubRoute,
	onSubRouteChange: (MainSubRoute) -> Unit
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		Crossfade(
			targetState = subRoute,
			modifier = Modifier
				.fillMaxWidth()
				.weight(1f),
			animationSpec = tween(durationMillis = 100),
			label = "MainPageCrossfade",
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

private val ItemSpacing = 6.dp
private val LeftNavigationWidth = 72.dp
private val LeftExpendedNavigationWidth = 160.dp
private val LeftNavigationItemHeight = 48.dp

@Composable
private fun BottomNavigationBar(
	subRoute: MainSubRoute,
	onSubRouteChange: (MainSubRoute) -> Unit
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(12.dp)
			.height(56.dp)
	) {
		val density = LocalDensity.current
		var width by remember { mutableStateOf(Dp.Unspecified) }
		if (width != Dp.Unspecified) {
			val sliderWidth by remember(width) {
				derivedStateOf {
					val size = MainSubRoute.entries.size
					(width - ItemSpacing * (size - 1)) / size
				}
			}
			val offsetXTarget by remember(sliderWidth, subRoute) {
				derivedStateOf {
					(sliderWidth + ItemSpacing) * MainSubRoute.entries.indexOfFirst { it == subRoute }
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
				.onSizeChanged {
					width = with(density) { it.width.toDp() }
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
						.clickable { onSubRouteChange(it) }
						.padding(horizontal = 4.dp),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.Center
				) {
					NoIcon(
						icon = it.icon.value(),
						tint = color,
						modifier = Modifier
							.size(24.dp)
					)
					Spacer(modifier = Modifier.width(8.dp))
					Text(
						text = it.title.value(),
						color = color,
						style = MaterialTheme.typography.bodyMedium,
						maxLines = 1,
						overflow = TextOverflow.Ellipsis
					)
				}
				if (index < MainSubRoute.entries.lastIndex) {
					Spacer(modifier = Modifier.width(ItemSpacing))
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
	val width by animateDpAsState(
		targetValue = if (isLeftNavigationBarExpended) LeftExpendedNavigationWidth else LeftNavigationWidth
	)
	val alpha by animateFloatAsState(
		targetValue = if (isLeftNavigationBarExpended) 0.25f else 0f
	)
	if (alpha > 0f) {
		Box(
			modifier = Modifier
				.padding(start = width)
				.fillMaxSize()
				.background(Color.Black.copy(alpha = alpha))
				.clickable(
					interactionSource = null,
					indication = null
				) {
					viewModel.isLeftNavigationBarExpended.value = false
				}
		)
	}
	NoWindowDraggableArea {
		BoxWithConstraints(
			modifier = Modifier
				.width(width)
				.fillMaxHeight()
				.background(MaterialTheme.colorScheme.surfaceContainer)
		) {
			var menuItemTop by remember { mutableStateOf(Dp.Unspecified) }
			LightingEffect(
				subRoute = subRoute,
				menuItemTop = menuItemTop,
			)
			Column(
				modifier = Modifier
					.fillMaxSize()
					.padding(
						top = 36.dp,
						start = 12.dp,
						end = 12.dp,
						bottom = 12.dp
					)
			) {
				val controller = LocalNavController.current
				var popStackEnabled by remember { mutableStateOf(false) }
				DisposableEffect(Unit) {
					val listener = NoNavHostController.OnDestinationChangedListener { controller, _, _ ->
						popStackEnabled = controller.currentRoute != MainRoute::class
					}
					controller.addOnDestinationChangedListener(listener)
					onDispose {
						controller.removeOnDestinationChangedListener(listener)
					}
				}
				val previousText = AppString.MainPrevious.value()
				LeftToolItem(
					title = previousText,
					icon = AppIcon.ArrowBack.value(),
					tooltipText = previousText,
					isExpended = isLeftNavigationBarExpended,
					onClick = {
						viewModel.isLeftNavigationBarExpended.value = false
						controller.popBackStack()
					},
					enabled = popStackEnabled
				)
				Spacer(modifier = Modifier.height(8.dp))
				Spacer(modifier = Modifier.weight(1f))
				val density = LocalDensity.current
				MainSubRoute.entries.forEachIndexed { index, route ->
					LeftMenuItem(
						title = route.title.value(),
						icon = route.icon.value(),
						tooltipText = route.title.value(),
						isExpended = isLeftNavigationBarExpended,
						onClick = {
							viewModel.isLeftNavigationBarExpended.value = false
							onSubRouteChange(route)
						},
						modifier = if (index > 0) Modifier else Modifier.onGloballyPositioned {
							menuItemTop = with(density) { it.positionInWindow().y.toDp() }
						},
						selected = subRoute == route
					)
					if (index < MainSubRoute.entries.lastIndex) {
						Spacer(modifier = Modifier.height(ItemSpacing))
					}
				}
				Spacer(modifier = Modifier.weight(1f))
				Spacer(modifier = Modifier.height(8.dp))
				val coroutineScope = rememberCoroutineScope()
				val isDark = ThemeMode.current.isDark
				val themeModeText =
					if (isDark) AppString.AppearanceThemeModeLight.value() else AppString.AppearanceThemeModeDark.value()
				LeftToolItem(
					title = themeModeText,
					icon = if (isDark) AppIcon.LightMode.value() else AppIcon.DarkMode.value(),
					tooltipText = themeModeText,
					isExpended = isLeftNavigationBarExpended,
					onClick = {
						coroutineScope.launch(Dispatchers.IO) {
							ThemeMode.set(if (isDark) ThemeMode.Light else ThemeMode.Dark)
						}
					},
					iconRotate = if (isDark) 90f else 0f,
				)
				Spacer(modifier = Modifier.width(6.dp))
				LeftToolItem(
					title = AppString.MainCollapse.value(),
					icon = AppIcon.KeyboardDoubleArrowRight.value(),
					tooltipText = AppString.MainExpand.value(),
					isExpended = isLeftNavigationBarExpended,
					onClick = {
						viewModel.isLeftNavigationBarExpended.value = !isLeftNavigationBarExpended
					},
					iconRotate = if (isLeftNavigationBarExpended) -180f else 0f,
				)
			}
		}
	}
}

@Composable
private fun LightingEffect(
	subRoute: MainSubRoute,
	menuItemTop: Dp
) {
	if (menuItemTop != Dp.Unspecified) {
		val targetOffsetY by remember(subRoute, menuItemTop) {
			derivedStateOf {
				menuItemTop + (LeftNavigationItemHeight + ItemSpacing) * subRoute.ordinal - 75.dp + LeftNavigationItemHeight / 2
			}
		}
		val offsetY by animateDpAsState(targetOffsetY)
		Box(
			modifier = Modifier
				.offset(y = offsetY)
				.fillMaxWidth()
				.height(150.dp)
				.blur(36.dp),
			contentAlignment = Alignment.CenterStart
		) {
			Box(
				modifier = Modifier
					.offset(x = (-10).dp)
					.size(40.dp)
					.clip(CircleShape)
					.background(MaterialTheme.colorScheme.primary)
			)
		}
	}
}

/**
 * 左侧菜单项
 */
@Composable
private fun LeftMenuItem(
	title: String,
	icon: ImageVector,
	tooltipText: String,
	isExpended: Boolean,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	selected: Boolean = false
) {
	LeftItemWithExpended(
		tooltipText = tooltipText,
		isExpended = isExpended
	) {
		val containerColor by animateColorAsState(
			targetValue = when {
				selected -> MaterialTheme.colorScheme.primary
				else -> Color.Transparent
			},
		)
		val contentColor by animateColorAsState(
			targetValue = when {
				selected -> MaterialTheme.colorScheme.onPrimary
				else -> MaterialTheme.colorScheme.primary
			},
		)
		Row(
			modifier = modifier
				.fillMaxWidth()
				.height(LeftNavigationItemHeight)
				.clip(MaterialTheme.shapes.small)
				.background(
					color = containerColor,
					shape = MaterialTheme.shapes.small
				)
				.clickable(
					onClick = onClick
				)
				.padding(horizontal = 12.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.Start
		) {
			NoIcon(
				icon = icon,
				modifier = Modifier.size(24.dp),
				tint = contentColor
			)
			Text(
				text = title,
				modifier = Modifier
					.padding(start = 8.dp)
					.weight(1f, fill = false),
				color = contentColor,
				style = MaterialTheme.typography.bodyMedium,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)
		}
	}
}

@Composable
private fun LeftToolItem(
	title: String,
	icon: ImageVector,
	tooltipText: String,
	isExpended: Boolean,
	onClick: () -> Unit,
	iconRotate: Float = 0f,
	enabled: Boolean = true
) {
	LeftItemWithExpended(
		tooltipText = tooltipText,
		isExpended = isExpended
	) {
		val primary = MaterialTheme.colorScheme.primary
		val contentColor by remember(enabled, primary) {
			derivedStateOf {
				if (enabled) primary else Color.Transparent
			}
		}
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.height(LeftNavigationItemHeight)
				.clip(MaterialTheme.shapes.small)
				.clickable(
					enabled = enabled,
					onClick = onClick
				)
				.padding(horizontal = 12.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.Start
		) {
			val iconRotate by animateFloatAsState(iconRotate)
			NoIcon(
				icon = icon,
				modifier = Modifier.size(24.dp)
					.rotate(iconRotate),
				tint = contentColor
			)
			Text(
				text = title,
				modifier = Modifier
					.padding(start = 8.dp)
					.weight(1f, fill = false),
				color = contentColor,
				style = MaterialTheme.typography.bodyMedium,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)
		}
	}
}

@Composable
private fun LeftItemWithExpended(
	tooltipText: String,
	isExpended: Boolean,
	content: @Composable () -> Unit
) {
	NoTooltipArea(
		tooltip = {
			Text(
				text = tooltipText,
				modifier = Modifier
					.clip(MaterialTheme.shapes.small)
					.shadow(
						elevation = 8.dp,
					)
					.border(
						width = 1.dp,
						color = Color(0xFF2B2D31),
						shape = MaterialTheme.shapes.small
					)
					.background(
						color = Color(0xFF25272C),
						shape = MaterialTheme.shapes.small
					)
					.padding(
						horizontal = 12.dp,
						vertical = 8.dp
					),
				color = Color.White,
				style = MaterialTheme.typography.bodyMedium,
				textAlign = TextAlign.Center
			)
		},
		delayMillis = if (isExpended) Int.MAX_VALUE else 200,
		tooltipPlacement = NoTooltipPlacement.ComponentRect(
			anchor = Alignment.CenterEnd,
			alignment = Alignment.CenterEnd,
			offset = DpOffset(16.dp, 0.dp)
		)
	) {
		content()
	}
}

enum class MainSubRoute(
	val title: AppString,
	val icon: AppIcon
) {
	Home(
		title = AppString.MainHome,
		icon = AppIcon.Home
	),
	Friends(
		title = AppString.MainFriends,
		icon = AppIcon.People
	),
	Groups(
		title = AppString.MainGroups,
		icon = AppIcon.Diversity2
	),
	Person(
		title = AppString.MainPerson,
		icon = AppIcon.Person
	)
}