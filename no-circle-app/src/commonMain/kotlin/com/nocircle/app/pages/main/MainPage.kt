package com.nocircle.app.pages.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
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
import com.nocircle.app.generated.resources.Res
import com.nocircle.app.generated.resources.login_success
import com.nocircle.app.generated.resources.main_home
import com.nocircle.app.generated.resources.main_person
import com.nocircle.app.pages.main.home.HomePage
import com.nocircle.app.pages.main.person.PersonPage
import com.nocircle.common.expends.WindowWidthSizes
import com.nocircle.common.expends.value
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoSnackbar
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
		snackbarHost = {
			SnackbarHost(hostState) {
				NoSnackbar(it)
			}
		}
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
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.weight(1f)
		) {
			when (mainRoute) {
				MainRoute.Home -> HomePage()
				MainRoute.Person -> PersonPage()
			}
		}
		BottomBar(
			mainRoute = mainRoute,
			onMainRouteChange = onMainRouteChange
		)
	}
}

@Composable
private fun BottomBar(
	mainRoute: MainRoute,
	onMainRouteChange: (MainRoute) -> Unit
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 12.dp, horizontal = 16.dp)
			.height(60.dp)
	) {
		val density = LocalDensity.current
		var sliderWidth by remember { mutableStateOf(Dp.Unspecified) }
		if (sliderWidth != Dp.Unspecified) {
			val offsetX by animateDpAsState(
				targetValue = sliderWidth * MainRoute.entries.indexOf(mainRoute)
			)
			Box(
				modifier = Modifier
					.offset(x = offsetX)
					.width(sliderWidth)
					.fillMaxHeight()
					.clip(CircleShape)
					.background(MaterialTheme.colorScheme.primary)
			)
		}
		Row(
			modifier = Modifier
				.fillMaxSize()
				.onGloballyPositioned {
					sliderWidth = with(density) { (it.size.width / MainRoute.entries.size).toDp() }
				}
		) {
			MainRoute.entries.forEach {
				val color by animateColorAsState(
					targetValue = if (it == mainRoute) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
				)
				Row(
					modifier = Modifier
						.weight(1f)
						.fillMaxHeight()
						.clip(CircleShape)
						.clickable {
							onMainRouteChange(it)
						},
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
						style = MaterialTheme.typography.titleMedium,
					)
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

}