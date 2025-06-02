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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
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
import com.nocircle.app.pages.settings.SettingsPage
import com.nocircle.app.pages.settings.SettingsRoute
import com.nocircle.app.pages.settings.appearance.AppearancePage
import com.nocircle.app.pages.settings.appearance.AppearanceRoute
import com.nocircle.app.pages.settings.appearance.AppearanceViewModel
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
}

private val ItemSpacing = 6.dp

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
                        .clickable { onSubRouteChange(it) }
                        .padding(horizontal = 4.dp),
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
    NoWindowDraggableArea {
        val viewModel = koinViewModel<MainViewModel>()
        val isLeftNavigationBarExpended by viewModel.isLeftNavigationBarExpended.collectAsState()
        val width by animateDpAsState(
            targetValue = if (isLeftNavigationBarExpended) 160.dp else 76.dp
        )
        Column(
            modifier = Modifier
                .width(width)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(
                    top = 36.dp,
                    start = 12.dp,
                    end = 12.dp,
                    bottom = 12.dp
                )
        ) {
            val controller = LocalNavController.current
            var popStackEnabled by remember { mutableStateOf(true) }
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
                icon = Icons.AutoMirrored.Outlined.ArrowBackIos,
                tooltipText = previousText,
                isExpended = isLeftNavigationBarExpended,
                onClick = { controller.popBackStack() },
                enabled = popStackEnabled
            )
            Spacer(modifier = Modifier.height(8.dp))
            Spacer(modifier = Modifier.weight(1f))
            MainSubRoute.entries.forEachIndexed { index, route ->
                LeftMenuItem(
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
                    Spacer(modifier = Modifier.height(ItemSpacing))
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(8.dp))
            val appearanceViewModel = koinViewModel<AppearanceViewModel>()
            val attribute by appearanceViewModel.colorSchemeAttribute.collectAsState()
            val isDark = attribute.themeMode.isDark
            val themeModeText =
                if (isDark) AppString.AppearanceThemeModeLight.value() else AppString.AppearanceThemeModeDark.value()
            LeftToolItem(
                title = themeModeText,
                icon = if (isDark) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
                tooltipText = themeModeText,
                isExpended = isLeftNavigationBarExpended,
                onClick = {
                    appearanceViewModel.colorSchemeAttribute.value = attribute.copy(
                        themeMode = ThemeMode.getThemeMode(!isDark)
                    )
                },
                iconRotate = if (isDark) 90f else 0f,
            )
            Spacer(modifier = Modifier.width(6.dp))
            LeftToolItem(
                title = AppString.MainCollapse.value(),
                icon = Icons.Outlined.KeyboardDoubleArrowRight,
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
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(MaterialTheme.shapes.small)
                .background(
                    color = containerColor,
                    shape = MaterialTheme.shapes.small
                )
                .clickable(
                    onClick = onClick
                )
                .padding(horizontal = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            NoIcon(
                icon = icon,
                modifier = Modifier.size(26.dp),
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
        val contentColor = remember(enabled, primary) {
            when (enabled) {
                true -> primary
                false -> Color.Transparent
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(MaterialTheme.shapes.small)
                .clickable(
                    enabled = enabled,
                    onClick = onClick
                )
                .padding(horizontal = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            val iconRotate by animateFloatAsState(iconRotate)
            NoIcon(
                icon = icon,
                modifier = Modifier.size(26.dp)
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
    val icon: ImageVector
) {
    Home(
        title = AppString.MainHome,
        icon = Icons.Outlined.Home
    ),
    Friends(
        title = AppString.MainFriends,
        icon = Icons.Outlined.People
    ),
    Groups(
        title = AppString.MainGroups,
        icon = Icons.Outlined.Diversity2
    ),
    Person(
        title = AppString.MainPerson,
        icon = Icons.Outlined.Person
    )
}