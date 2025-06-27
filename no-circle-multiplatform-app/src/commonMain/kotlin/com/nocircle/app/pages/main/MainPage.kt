package com.nocircle.app.pages.main

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nocircle.app.pages.friends.FriendsPage
import com.nocircle.app.pages.groups.GroupsPage
import com.nocircle.app.pages.home.HomePage
import com.nocircle.app.pages.main.navigation.BottomNavigationBar
import com.nocircle.app.pages.main.navigation.LeftNavigationBar
import com.nocircle.app.pages.main.navigation.LeftNavigationWidth
import com.nocircle.app.pages.person.PersonPage
import com.nocircle.app.pages.person.message.MessageCenterPage
import com.nocircle.app.pages.person.message.MessageCenterRoute
import com.nocircle.app.pages.settings.SettingsPage
import com.nocircle.app.pages.settings.SettingsRoute
import com.nocircle.app.pages.settings.about.AboutPage
import com.nocircle.app.pages.settings.about.AboutRoute
import com.nocircle.app.pages.settings.appearance.AppearancePage
import com.nocircle.app.pages.settings.appearance.AppearanceRoute
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.showNoSnackbar
import com.nocircle.compose.navigation.*
import com.nocircle.compose.resources.NoIcon
import com.nocircle.compose.windowsize.WindowWidthSizes
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object MainRoute : NoRoute

@Composable
fun MainPage() {
	val viewModel = koinViewModel<MainViewModel>()
	val hostState = remember { SnackbarHostState() }
	LaunchedEffect(Unit) {
		hostState.showNoSnackbar(AppString.LOGIN_SUCCESS)
		viewModel.snackbarCollect(hostState::showNoSnackbar)
	}
	NoScaffold(
		snackbarHostState = hostState,
	) {
		val isCompact = WindowWidthSizes.isCompact
		val subRoute by viewModel.mainSubRoute.collectAsState()
		LocalNavControllerProvider(MainRoute) { controller ->
			val onSubRouteChange = { route: MainSubRoute ->
				if (controller.isNotRoute<MainRoute>()) {
					controller.popBackStack(MainRoute, false)
				}
				if (subRoute != route) {
					viewModel.mainSubRoute.value = route
				}
			}
			if (!isCompact) {
				LeftNavigationBar(
					subRoute = subRoute,
					onSubRouteChange = onSubRouteChange
				)
			}
			NavHost(
				navController = controller,
				startDestination = MainRoute,
				modifier = Modifier
					.padding(start = if (isCompact) Dp.Hairline else LeftNavigationWidth)
					.shadow(
						elevation = 4.dp,
						spotColor = MaterialTheme.colorScheme.outlineVariant,
						ambientColor = MaterialTheme.colorScheme.outlineVariant
					)
					.background(MaterialTheme.colorScheme.surface)
					.fillMaxSize(),
				enterTransition = enterTransition { if (isCompact) horizontalSlider() else fade() },
				exitTransition = exitTransition { if (isCompact) horizontalSlider() else fade() },
				popEnterTransition = popEnterTransition { if (isCompact) horizontalSlider() else fade() },
				popExitTransition = popExitTransition { if (isCompact) horizontalSlider() else fade() }
			) {
				composable<MainRoute> {
					MainPage(
						subRoute = subRoute,
						onSubRouteChange = onSubRouteChange
					)
				}
				composable<SettingsRoute> { SettingsPage() }
				composable<AppearanceRoute> { AppearancePage() }
				composable<MessageCenterRoute> { MessageCenterPage() }
				composable<AboutRoute> { AboutPage() }
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
		) { subRoute ->
			when (subRoute) {
				MainSubRoute.HOME -> HomePage()
				MainSubRoute.FRIENDS -> FriendsPage()
				MainSubRoute.GROUPS -> GroupsPage()
				MainSubRoute.PERSON -> PersonPage()
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

enum class MainSubRoute(
	val title: AppString,
	val icon: NoIcon
) {
	HOME(
		title = AppString.MAIN_HOME,
		icon = AppIcon.Home
	),
	FRIENDS(
		title = AppString.MAIN_FRIENDS,
		icon = AppIcon.Group
	),
	GROUPS(
		title = AppString.MAIN_GROUPS,
		icon = AppIcon.Diversity2
	),
	PERSON(
		title = AppString.MAIN_PERSON,
		icon = AppIcon.Person
	)
}