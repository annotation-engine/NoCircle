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
import androidx.compose.ui.text.style.TextOverflow
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
				val onSubRouteChange = { route: MainSubRoute ->
					if (subRoute != route) {
						viewModel.mainSubRoute.value = route
					}
					if (controller.currentRoute != MainRoute::class) {
						controller.navigate(route = MainRoute, popup = NoPopUp.All)
					}
				}
				if (!isCompat) {
					LeftNavigationBar(
						subRoute = subRoute,
						onSubRouteChange = onSubRouteChange
					)
				}
				NoNavHost(
					navController = controller,
					startDestination = MainRoute,
					modifier = Modifier
						.weight(1f)
						.fillMaxHeight(),
					navTransition = if (isCompat) NavTransition.HorizontalSlide else NavTransition.Fade,
					navPopTransition = if (isCompat) NavPopTransition.HorizontalSlide else NavPopTransition.Fade,
				) {
					composable<MainRoute>(
						content = {
							MainPage(
								subRoute = subRoute,
								onSubRouteChange = onSubRouteChange
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
	NoScaffold {
		val isCompat = WindowWidthSizes.isCompact
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(
					top = if (isCompat) it.calculateTopPadding() else Dp.Hairline
				)
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
			.height(56.dp)
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
							.size(24.dp)
					)
					Spacer(modifier = Modifier.width(8.dp))
					Text(
						text = it.title.value(),
						color = color,
						style = MaterialTheme.typography.bodyMedium,
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
	val width by animateDpAsState(
		targetValue = if (isLeftNavigationBarExpended) 140.dp else 68.dp
	)
	Column(
		modifier = Modifier
			.width(width)
			.fillMaxHeight()
			.background(MaterialTheme.colorScheme.surfaceContainer)
			.padding(
				top = 32.dp,
				start = 12.dp,
				end = 12.dp,
				bottom = 12.dp
			)
	) {
		val controller = LocalNavController.current
		val previousText = Res.string.main_previous.value()
		LeftBarItem(
			title = previousText,
			icon = Icons.AutoMirrored.Rounded.ArrowBackIos,
			tooltipText = previousText,
			isExpended = isLeftNavigationBarExpended,
			onClick = {
				controller.popBackStack()
			}
		)
		Spacer(modifier = Modifier.height(8.dp))
		Spacer(modifier = Modifier.weight(1f))
		MainSubRoute.entries.forEachIndexed { index, route ->
			LeftBarItem(
				title = route.title.value(),
				icon = route.icon,
				tooltipText = route.title.value(),
				isExpended = isLeftNavigationBarExpended,
				onClick = {
					onSubRouteChange(route)
				},
				selected = subRoute == route
			)
			if (index < MainSubRoute.entries.lastIndex) {
				Spacer(modifier = Modifier.height(8.dp))
			}
		}
		Spacer(modifier = Modifier.weight(1f))
		Spacer(modifier = Modifier.height(8.dp))
		val appearanceViewModel = koinViewModel<AppearanceViewModel>()
		val attribute by appearanceViewModel.colorSchemeAttribute.collectAsState()
		val isDark = attribute.themeMode.isDark
		val themeModeText = if (isDark) Res.string.appearance_theme_mode_light.value() else Res.string.appearance_theme_mode_dark.value()
		LeftBarItem(
			title = themeModeText,
			icon = if (isDark) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
			tooltipText = themeModeText,
			isExpended = isLeftNavigationBarExpended,
			onClick = {
				appearanceViewModel.colorSchemeAttribute.value = attribute.copy(
					themeMode = ThemeMode.getThemeMode(!isDark)
				)
			}
		)
		Spacer(modifier = Modifier.width(6.dp))
		LeftBarItem(
			title = Res.string.main_collapse.value(),
			icon = if (isLeftNavigationBarExpended) Icons.Rounded.KeyboardDoubleArrowLeft else Icons.Rounded.KeyboardDoubleArrowRight,
			tooltipText = Res.string.main_expended.value(),
			isExpended = isLeftNavigationBarExpended,
			onClick = {
				viewModel.isLeftNavigationBarExpended.value = !isLeftNavigationBarExpended
			}
		)
	}
}

@Composable
private fun LeftBarItem(
	title: String,
	icon: ImageVector,
	tooltipText: String,
	isExpended: Boolean,
	onClick: () -> Unit,
	enabled: Boolean = true,
	selected: Boolean = false
) {
	LeftBarItemTooltip(
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
				!enabled -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f)
				selected -> MaterialTheme.colorScheme.onPrimary
				else -> MaterialTheme.colorScheme.tertiary
			},
		)
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.height(44.dp)
				.clip(MaterialTheme.shapes.small)
				.background(
					color = containerColor,
					shape = MaterialTheme.shapes.small
				)
				.clickable(onClick = onClick)
				.padding(8.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.Start
		) {
			NoIcon(
				icon = icon,
				modifier = Modifier.size(28.dp),
				tint = contentColor
			)
			Spacer(modifier = Modifier.width(8.dp))
			Text(
				text = title,
				color = contentColor,
				style = MaterialTheme.typography.bodyMedium,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis
			)
		}
	}
}

@Composable
expect fun LeftBarItemTooltip(
	tooltipText: String,
	isExpended: Boolean,
	content: @Composable () -> Unit
)

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
		icon = Icons.Rounded.People
	),
	Groups(
		title = Res.string.main_groups,
		icon = Icons.Rounded.Diversity2
	),
	Person(
		title = Res.string.main_person,
		icon = Icons.Rounded.Person
	)
}