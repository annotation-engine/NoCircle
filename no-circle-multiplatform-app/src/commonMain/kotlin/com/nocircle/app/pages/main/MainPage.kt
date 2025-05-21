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
import com.nocircle.app.generated.resources.*
import com.nocircle.app.pages.main.friend.FriendPage
import com.nocircle.app.pages.main.group.GroupPage
import com.nocircle.app.pages.main.home.HomePage
import com.nocircle.app.pages.main.person.PersonNavHostKey
import com.nocircle.app.pages.main.person.PersonPage
import com.nocircle.common.navigation.NoNavControllerManager
import com.nocircle.common.navigation.NoRoute
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

@Composable
fun MainPage() {
	val viewModel = koinViewModel<MainViewModel>()
	val hostState = remember { SnackbarHostState() }
	val controller = NoNavControllerManager.get()
	LaunchedEffect(Unit) {
		if (controller.resultRoute == null) {
			hostState.showNoSnackbar(Res.string.login_success)
		}
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
			val subPage by viewModel.mainSubPage.collectAsState()
			if (WindowWidthSizes.isCompact) {
				CompactMainPage(
					subPage = subPage,
					onSubPageChange = { viewModel.mainSubPage.value = it },
				)
			} else {
				MediumMainPage(
					subPage = subPage,
					onSubPageChange = { viewModel.mainSubPage.value = it },
				)
			}
		}
	}
}

@Composable
private fun CompactMainPage(
	subPage: MainSubPage,
	onSubPageChange: (MainSubPage) -> Unit
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		Box(
			modifier = Modifier
				.weight(1f)
				.fillMaxHeight()
		) {
			MainRoute(subPage)
		}
		BottomBar(
			subPage = subPage,
			onSubPageChange = onSubPageChange
		)
	}
}

private val HorizontalItemSpacing = 12.dp

@Composable
private fun BottomBar(
	subPage: MainSubPage,
	onSubPageChange: (MainSubPage) -> Unit
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
					val size = MainSubPage.entries.size
					(width - HorizontalItemSpacing * (size - 1)) / size
				}
			}
			val offsetXTarget by remember(sliderWidth, subPage) {
				derivedStateOf {
					(sliderWidth + HorizontalItemSpacing) * MainSubPage.entries.indexOfFirst { it == subPage }
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
			MainSubPage.entries.fastForEachIndexed { index, it ->
				val color by animateColorAsState(
					targetValue = if (it == subPage) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.secondary
				)
				Row(
					modifier = Modifier
						.weight(1f)
						.fillMaxHeight()
						.clip(MaterialTheme.shapes.small)
						.clickable { onSubPageChange(it) },
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
				if (index < MainSubPage.entries.lastIndex) {
					Spacer(modifier = Modifier.width(HorizontalItemSpacing))
				}
			}
		}
	}
}

@Composable
private fun MediumMainPage(
	subPage: MainSubPage,
	onSubPageChange: (MainSubPage) -> Unit
) {
	Row(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.surfaceContainer),
		verticalAlignment = Alignment.CenterVertically,
	) {
		LeftBar(
			subPage = subPage,
			onSubPageChange = onSubPageChange
		)
		Box(
			modifier = Modifier
				.weight(1f)
				.fillMaxHeight()
				.background(MaterialTheme.colorScheme.surface)
		) {
			MainRoute(subPage)
		}
	}
}

private val VerticalItemSpacing = 8.dp

@Composable
private fun LeftBar(
	subPage: MainSubPage,
	onSubPageChange: (MainSubPage) -> Unit
) {
	Box(
		modifier = Modifier
			.width(68.dp)
			.fillMaxHeight()
			.padding(
				horizontal = 8.dp,
				vertical = 32.dp
			)
	) {
		val navController = NoNavControllerManager.get(
			moreNavHost = when (subPage) {
				MainSubPage.Person -> PersonNavHostKey
				else -> PersonNavHostKey
			}
		)
		NoIconButton(
			icon = Icons.AutoMirrored.Rounded.ArrowBackIos,
			modifier = Modifier
				.align(Alignment.TopCenter),
			tint = MaterialTheme.colorScheme.primary
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
						val size = MainSubPage.entries.size
						(height - VerticalItemSpacing * (size - 1)) / size
					}
				}
				val offsetYTarget by remember(sliderHeight, subPage) {
					derivedStateOf {
						(sliderHeight + VerticalItemSpacing) * MainSubPage.entries.indexOfFirst { it == subPage }
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
				MainSubPage.entries.fastForEachIndexed { index, it ->
					val color by animateColorAsState(
						targetValue = if (it == subPage) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.secondary
					)
					Column(
						modifier = Modifier
							.fillMaxWidth()
							.clip(MaterialTheme.shapes.small)
							.clickable { onSubPageChange(it) }
							.padding(vertical = 6.dp),
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
					if (index < MainSubPage.entries.size - 1) {
						Spacer(modifier = Modifier.height(VerticalItemSpacing))
					}
				}
			}
		}
	}
}

@Composable
private fun MainRoute(
	subPage: MainSubPage,
) {
	Crossfade(
		targetState = subPage,
		animationSpec = tween(durationMillis = 120),
		label = "CompactMainRouteCrossfade",
	) { target ->
		when (target) {
			MainSubPage.Home -> HomePage()
			MainSubPage.Friend -> FriendPage()
			MainSubPage.Group -> GroupPage()
			MainSubPage.Person -> PersonPage()
		}
	}
}

enum class MainSubPage(
	val title: StringResource,
	val icon: ImageVector
) {
	
	Home(
		title = Res.string.main_home,
		icon = Icons.Rounded.Home
	),
	
	Friend(
		title = Res.string.main_friend,
		icon = Icons.Rounded.Person2
	),
	
	Group(
		title = Res.string.main_group,
		icon = Icons.Rounded.Group
	),
	
	Person(
		title = Res.string.main_person,
		icon = Icons.Rounded.Person
	)
}