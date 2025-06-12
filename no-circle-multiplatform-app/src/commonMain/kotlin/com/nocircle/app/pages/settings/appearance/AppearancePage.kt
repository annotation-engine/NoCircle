package com.nocircle.app.pages.settings.appearance

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.app.theme.colors.ColorSchemeContrast
import com.nocircle.app.theme.colors.ColorSchemeGroup
import com.nocircle.app.theme.colors.ThemeMode
import com.nocircle.app.theme.colors.getColorScheme
import com.nocircle.common.navigation.LocalNavController
import com.nocircle.common.navigation.NoRoute
import com.nocircle.common.resources.value
import com.nocircle.common.windowsize.WindowWidthSizes
import com.nocircle.common.windowsize.calculateWindowWidthSize
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoTopAppBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object AppearanceRoute : NoRoute

@Composable
fun AppearancePage() {
	val controller = LocalNavController.current
	NoScaffold(
		topBar = {
			NoTopAppBar(
				title = { Text(AppString.APPEARANCE.value()) },
				onNavigationIconClick = if (WindowWidthSizes.isCompact) {
					{ controller.popBackStack() }
				} else null
			)
		},
	) { paddingValues ->
		val verticalScrollState = rememberScrollState()
		val overscrollEffect = rememberOverscrollEffect()
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(top = paddingValues.calculateTopPadding())
				.verticalScroll(verticalScrollState)
				.overscroll(overscrollEffect),
			contentAlignment = Alignment.TopCenter
		) {
			Column(
				modifier = Modifier
					.widthIn(max = 840.dp)
					.fillMaxSize()
					.padding(
						horizontal = 4.dp,
						vertical = 16.dp
					),
			) {
				ColorSchemeContrastOptions()
				ThemeModeOptions()
				ColorSchemeGroupOptions()
			}
		}
	}
}

/**
 * 对比度
 */
@Composable
private fun ColorSchemeContrastOptions() {
	val current = ColorSchemeContrast.current
	val coroutineScope = rememberCoroutineScope()
	SettingsOptions(
		icon = AppIcon.Contrast.value(),
		title = AppString.APPEARANCE_CONTRAST.value(),
		items = ColorSchemeContrast.entries,
		current = current
	) { contrast ->
		val colorScheme = getColorScheme(contrast = contrast)
		ColorSchemeCard(
			selected = current == contrast,
			colorScheme = colorScheme,
			name = contrast.title.value(),
			preview = AppString.APPEARANCE_CONTRAST_PREVIEW.value()
		) {
			coroutineScope.launch(Dispatchers.IO) {
				ColorSchemeContrast.set(contrast)
			}
		}
	}
}

/**
 * 主题色
 */
@Composable
private fun ColorSchemeGroupOptions() {
	val current = ColorSchemeGroup.current
	val coroutineScope = rememberCoroutineScope()
	SettingsOptions(
		icon = AppIcon.ColorLens.value(),
		title = AppString.APPEARANCE_THEME.value(),
		items = ColorSchemeGroup.allColorSchemeGroups,
		current = current
	) { group ->
		val colorScheme = getColorScheme(group = group)
		ColorSchemeCard(
			selected = current == group,
			colorScheme = colorScheme,
			name = group.name.value(),
			preview = AppString.APPEARANCE_THEME_PREVIEW.value()
		) {
			coroutineScope.launch(Dispatchers.IO) {
				ColorSchemeGroup.set(group)
			}
		}
	}
}

/**
 * 主题模式
 */
@Composable
private fun ThemeModeOptions() {
	val viewModel = koinViewModel<AppearanceViewModel>()
	val themeModeSystemPercent = viewModel.themeModeSystemPercent
	val isDarkPreview by viewModel.isDarkPreview.collectAsState()
	LaunchedEffect(Unit) {
		while (true) {
			themeModeSystemPercent.snapTo(0f)
			themeModeSystemPercent.animateTo(
				targetValue = 1f,
				animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing)
			)
			delay(500)
			viewModel.isDarkPreview.value = !isDarkPreview
		}
	}
	val current = ThemeMode.current
	val coroutineScope = rememberCoroutineScope()
	SettingsOptions(
		icon = AppIcon.DarkMode.value(),
		title = AppString.APPEARANCE_THEME_MODE.value(),
		current = current,
		items = ThemeMode.entries
	) { themeMode ->
		if (themeMode != ThemeMode.SYSTEM) {
			val colorScheme = getColorScheme(themeMode = themeMode)
			ColorSchemeCard(
				selected = current == themeMode,
				colorScheme = colorScheme,
				name = themeMode.title.value(),
				preview = AppString.APPEARANCE_THEME_MODE_PREVIEW.value()
			) {
				coroutineScope.launch(Dispatchers.IO) {
					ThemeMode.set(themeMode)
				}
			}
		} else {
			Box(
				modifier = Modifier
					.clip(MaterialTheme.shapes.large)
					.clickable {
						coroutineScope.launch(Dispatchers.IO) {
							ThemeMode.set(ThemeMode.SYSTEM)
						}
					}
			) {
				BooleanList.forEach { isDark ->
					val percent = themeModeSystemPercent.value
					val shape by remember(percent) {
						derivedStateOf {
							GenericShape { size, _ ->
								moveTo(size.width * percent, 0f)
								lineTo(size.width * percent, size.height)
								if (isDark) {
									lineTo(size.width, size.height)
									lineTo(size.width, 0f)
								} else {
									lineTo(0f, size.height)
									lineTo(0f, 0f)
								}
								close()
							}
						}
					}
					val themeMode by remember(isDarkPreview, isDark) {
						derivedStateOf {
							if ((isDarkPreview && isDark) || (!isDarkPreview && !isDark)) ThemeMode.DARK else ThemeMode.LIGHT
						}
					}
					val colorScheme = getColorScheme(themeMode = themeMode)
					ColorSchemeCard(
						selected = current == ThemeMode.SYSTEM,
						colorScheme = colorScheme,
						name = ThemeMode.SYSTEM.title.value(),
						preview = AppString.APPEARANCE_THEME_MODE_PREVIEW.value(),
						modifier = Modifier.clip(shape)
					)
				}
			}
		}
	}
}

private val BooleanList = arrayOf(true, false)

/**
 * 设置选项
 */
@Composable
private fun <T : Any> SettingsOptions(
	icon: ImageVector,
	title: String,
	items: List<T>,
	current: T,
	content: @Composable (item: T) -> Unit
) {
	var singleLine by remember { mutableStateOf(true) }
	val windowWidthSize = calculateWindowWidthSize()
	val displayCount by remember(windowWidthSize) {
		derivedStateOf {
			when (windowWidthSize) {
				WindowWidthSizeClass.Compact -> 2
				WindowWidthSizeClass.Medium -> 3
				else -> 4
			}
		}
	}
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		NoIcon(
			icon = icon,
			modifier = Modifier.size(24.dp),
			tint = MaterialTheme.colorScheme.primary
		)
		Spacer(Modifier.width(16.dp))
		Text(
			text = title,
			color = MaterialTheme.colorScheme.onSurface,
			style = MaterialTheme.typography.titleMedium
		)
		if (items.size > displayCount) {
			Spacer(Modifier.weight(1f))
			Row(
				modifier = Modifier
					.clip(MaterialTheme.shapes.small)
					.clickable {
						singleLine = !singleLine
					}
					.padding(
						start = 12.dp,
						end = 4.dp,
						top = 2.dp,
						bottom = 2.dp
					),
				verticalAlignment = Alignment.CenterVertically,
			) {
				Text(
					text = if (singleLine) AppString.APPEARANCE_MULTI_LINE.value(items.size) else AppString.APPEARANCE_SINGLE_LINE.value(),
					color = MaterialTheme.colorScheme.primary,
					style = MaterialTheme.typography.bodyMedium
				)
				Spacer(Modifier.width(4.dp))
				val degrees by animateFloatAsState(
					targetValue = if (singleLine) 0f else 90f
				)
				NoIcon(
					icon = AppIcon.KeyboardArrowRight.value(),
					modifier = Modifier
						.rotate(degrees),
					tint = MaterialTheme.colorScheme.primary
				)
			}
		}
	}
	if (singleLine) {
		SingleLineOptions(
			items = items,
			current = current,
			displayCount = displayCount
		) {
			content(it)
		}
	} else {
		MultiLineOptions(
			items = items,
			columnCount = displayCount
		) {
			content(it)
		}
	}
	Spacer(Modifier.height(16.dp))
}

/**
 * 主题卡片
 */
@Composable
private fun ColorSchemeCard(
	selected: Boolean,
	colorScheme: ColorScheme,
	name: String,
	preview: String,
	modifier: Modifier = Modifier,
	onClick: (() -> Unit)? = null,
) {
	val borderColor = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent
	MaterialTheme(colorScheme) {
		val interactionSource = remember { MutableInteractionSource() }
		val isHovered by interactionSource.collectIsHoveredAsState()
		val isPressed by interactionSource.collectIsPressedAsState()
		val scale by animateFloatAsState(
			targetValue = when {
				isPressed -> 0.99f
				isHovered -> 1.01f
				else -> 1f
			},
			animationSpec = tween(durationMillis = 150)
		)
		Column(
			modifier = Modifier
				.scale(scale)
				.fillMaxWidth()
				.clip(MaterialTheme.shapes.large)
				.then(
					if (onClick == null) Modifier else Modifier.clickable(
						interactionSource = interactionSource,
						indication = LocalIndication.current,
						onClick = onClick
					)
				)
				.border(
					width = 3.dp,
					color = borderColor,
					shape = MaterialTheme.shapes.large
				)
				.then(modifier)
				.padding(6.dp)
				.clip(MaterialTheme.shapes.medium)
				.background(
					color = MaterialTheme.colorScheme.primaryContainer,
					shape = MaterialTheme.shapes.medium
				)
				.hoverable(interactionSource)
		) {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.height(80.dp)
			) {
				val colors = arrayOf(
					MaterialTheme.colorScheme.primary,
					MaterialTheme.colorScheme.primaryContainer,
					MaterialTheme.colorScheme.secondary,
					MaterialTheme.colorScheme.secondaryContainer,
					MaterialTheme.colorScheme.tertiary,
					MaterialTheme.colorScheme.tertiaryContainer,
					MaterialTheme.colorScheme.inverseSurface,
					MaterialTheme.colorScheme.surface,
					MaterialTheme.colorScheme.error,
					MaterialTheme.colorScheme.errorContainer
				)
				repeat(5) { col ->
					Column(
						modifier = Modifier
							.weight(1f)
							.fillMaxHeight()
					) {
						repeat(2) { row ->
							val backgroundColor = colors[col * 2 + row]
							Box(
								modifier = Modifier
									.fillMaxWidth()
									.weight(1f)
									.background(backgroundColor),
								contentAlignment = Alignment.Center
							) {
								Text(
									text = "${preview[col]}",
									color = contentColorFor(backgroundColor),
									style = MaterialTheme.typography.bodyMedium
								)
							}
						}
					}
				}
			}
			Spacer(Modifier.height(4.dp))
			Row(
				modifier = Modifier
					.padding(8.dp),
				verticalAlignment = Alignment.CenterVertically,
			) {
				Text(
					text = name,
					color = MaterialTheme.colorScheme.onPrimaryContainer,
					style = MaterialTheme.typography.bodyMedium,
					maxLines = 1,
					overflow = TextOverflow.Ellipsis
				)
			}
		}
	}
}

private val IntervalDp = 4.dp

/**
 * 单行显示组件
 */
@Composable
private fun <T> SingleLineOptions(
	items: List<T>,
	current: T,
	displayCount: Int,
	content: @Composable (item: T) -> Unit,
) {
	val density = LocalDensity.current
	val viewModel = koinViewModel<AppearanceViewModel>()
	val cardWidth by viewModel.colorSchemeCardWidth.collectAsState()
	val lazyListState = rememberLazyListState()
	val scrollOffset = with(density) { (-6).dp.roundToPx() }
	LaunchedEffect(Unit) {
		lazyListState.requestScrollToItem(items.indexOf(current), scrollOffset = scrollOffset)
	}
	LazyRow(
		modifier = Modifier
			.fillMaxWidth()
			.onSizeChanged {
				viewModel.colorSchemeCardWidth.value = with(density) {
					(it.width.toDp() - 12.dp - IntervalDp * (displayCount - 1)) / displayCount
				}
			},
		state = lazyListState
	) {
		itemsIndexed(items) { index, item ->
			if (cardWidth == Dp.Unspecified) return@itemsIndexed
			if (index == 0) {
				Spacer(modifier = Modifier.width(6.dp))
			}
			Box(
				modifier = Modifier
					.width(cardWidth)
			) {
				content(item)
			}
			if (index < items.lastIndex) {
				Spacer(modifier = Modifier.width(IntervalDp))
			} else {
				Spacer(modifier = Modifier.width(6.dp))
			}
		}
	}
}

/**
 * 多行显示组件
 */
@Composable
private fun <T> MultiLineOptions(
	items: List<T>,
	columnCount: Int,
	content: @Composable (item: T) -> Unit,
) {
	val allItems by remember(items, columnCount) {
		derivedStateOf { items.chunked(columnCount) }
	}
	val viewModel = koinViewModel<AppearanceViewModel>()
	val cardWidth by viewModel.colorSchemeCardWidth.collectAsState()
	Column(
		modifier = Modifier
			.fillMaxWidth()
	) {
		allItems.forEachIndexed { index, items ->
			Row(
				modifier = Modifier
					.fillMaxWidth()
			) {
				Spacer(modifier = Modifier.width(6.dp))
				items.forEachIndexed { index, item ->
					Box(
						modifier = Modifier.width(cardWidth)
					) {
						content(item)
					}
					if (index < items.lastIndex) {
						Spacer(modifier = Modifier.width(IntervalDp))
					}
				}
				Spacer(modifier = Modifier.width(6.dp))
			}
			if (index < allItems.lastIndex) {
				Spacer(modifier = Modifier.height(IntervalDp))
			}
		}
	}
}