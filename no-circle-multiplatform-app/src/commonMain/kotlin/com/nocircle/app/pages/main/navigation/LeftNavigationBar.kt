package com.nocircle.app.pages.main.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.nocircle.app.pages.main.MainRoute
import com.nocircle.app.pages.main.MainSubRoute
import com.nocircle.app.pages.main.MainViewModel
import com.nocircle.app.pages.person.PersonViewModel
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.app.theme.scheme.ColorSchemeConfig
import com.nocircle.app.theme.scheme.ThemeMode
import com.nocircle.compose.desktop.NoTooltipArea
import com.nocircle.compose.desktop.NoTooltipPlacement
import com.nocircle.compose.desktop.NoWindowDraggableArea
import com.nocircle.compose.desktop.TooltipText
import com.nocircle.compose.foundation.NoAsyncImage
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.navigation.LocalNavController
import com.nocircle.compose.navigation.isNotRoute
import com.nocircle.compose.resources.value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LeftNavigationBar(
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
				.zIndex(1f)
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
	val index by remember(alpha) {
		derivedStateOf { if (alpha == 0f) 0f else 1f }
	}
	NoWindowDraggableArea(
		modifier = Modifier
			.zIndex(index)
	) {
		BoxWithConstraints(
			modifier = Modifier
				.width(width)
				.fillMaxHeight()
				.background(MaterialTheme.colorScheme.surfaceContainerLow)
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
					val listener = NavController.OnDestinationChangedListener { controller, _, _ ->
						popStackEnabled = controller.isNotRoute<MainRoute>()
					}
					controller.addOnDestinationChangedListener(listener)
					onDispose {
						controller.removeOnDestinationChangedListener(listener)
					}
				}
				if (popStackEnabled) {
					val previousText = AppString.MAIN_PREVIOUS.value()
					LeftToolItem(
						title = previousText,
						icon = AppIcon.ArrowBack.value(),
						tooltipText = previousText,
						isExpended = isLeftNavigationBarExpended,
						onClick = {
							viewModel.isLeftNavigationBarExpended.value = false
							controller.popBackStack()
						}
					)
				} else {
					val personViewModel = koinViewModel<PersonViewModel>()
					val userDetail by personViewModel.userDetail.collectAsState()
					if (userDetail != null) {
						NoAsyncImage(
							url = userDetail!!.avatarUrl,
							modifier = Modifier
								.size(LeftNavigationItemHeight)
								.clip(MaterialTheme.shapes.medium)
								.clickable {
									onSubRouteChange(MainSubRoute.PERSON)
								},
							contentScale = ContentScale.Crop,
						)
					}
				}
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
				val config = ColorSchemeConfig.current
				val isDark = config.themeMode.isDark
				val themeModeText =
					if (isDark) AppString.APPEARANCE_THEME_MODE_LIGHT.value() else AppString.APPEARANCE_THEME_MODE_DARK.value()
				LeftToolItem(
					title = themeModeText,
					icon = if (isDark) AppIcon.LightMode.value() else AppIcon.DarkMode.value(),
					tooltipText = themeModeText,
					isExpended = isLeftNavigationBarExpended,
					onClick = {
						coroutineScope.launch(Dispatchers.IO) {
							val themeMode = if (isDark) ThemeMode.LIGHT else ThemeMode.DARK
							ColorSchemeConfig.update(config.copy(themeMode = themeMode))
						}
					},
					iconRotate = if (isDark) 90f else 0f,
				)
				Spacer(modifier = Modifier.width(6.dp))
				LeftToolItem(
					title = AppString.MAIN_COLLAPSE.value(),
					icon = AppIcon.KeyboardDoubleArrowRight.value(),
					tooltipText = AppString.MAIN_EXPEND.value(),
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
				menuItemTop + (LeftNavigationItemHeight + ItemSpacing) * subRoute.ordinal - 80.dp + LeftNavigationItemHeight / 2
			}
		}
		val offsetY by animateDpAsState(targetOffsetY)
		Box(
			modifier = Modifier
				.offset(y = offsetY)
				.fillMaxWidth()
				.height(160.dp)
				.blur(36.dp),
			contentAlignment = Alignment.CenterStart
		) {
			Box(
				modifier = Modifier
					.offset(x = (-20).dp)
					.size(50.dp)
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
			targetValue = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent
		)
		val contentColor by animateColorAsState(
			targetValue = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
		)
		Row(
			modifier = modifier
				.fillMaxWidth()
				.height(LeftNavigationItemHeight)
				.clip(MaterialTheme.shapes.medium)
				.background(
					color = containerColor,
					shape = MaterialTheme.shapes.medium
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
	iconRotate: Float = 0f
) {
	LeftItemWithExpended(
		tooltipText = tooltipText,
		isExpended = isExpended
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.height(LeftNavigationItemHeight)
				.clip(MaterialTheme.shapes.medium)
				.clickable(onClick = onClick)
				.padding(horizontal = 12.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.Start
		) {
			val iconRotate by animateFloatAsState(iconRotate)
			NoIcon(
				icon = icon,
				modifier = Modifier.size(24.dp)
					.rotate(iconRotate),
				tint = MaterialTheme.colorScheme.primary
			)
			Text(
				text = title,
				modifier = Modifier
					.padding(start = 8.dp)
					.weight(1f, fill = false),
				color = MaterialTheme.colorScheme.primary,
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
		tooltip = { TooltipText(tooltipText) },
		delayMillis = if (isExpended) Int.MAX_VALUE else 200,
		tooltipPlacement = NoTooltipPlacement.ComponentRect(
			anchor = Alignment.CenterEnd,
			alignment = Alignment.CenterEnd,
			offset = DpOffset(16.dp, Dp.Hairline)
		)
	) {
		content()
	}
}

private val ItemSpacing = 6.dp
val LeftNavigationWidth = 72.dp
private val LeftExpendedNavigationWidth = 160.dp
private val LeftNavigationItemHeight = 48.dp