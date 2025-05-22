package com.nocircle.app.pages.settings.appearance

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.Contrast
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nocircle.app.constants.ColorSchemeContrastConfigKey
import com.nocircle.app.constants.ColorSchemeGroupConfigKey
import com.nocircle.app.constants.ColorSchemeThemeModeConfigKey
import com.nocircle.app.generated.resources.*
import com.nocircle.app.theme.colors.ColorSchemeContrast
import com.nocircle.app.theme.colors.ColorSchemeGroup
import com.nocircle.app.theme.colors.ThemeMode
import com.nocircle.common.config.set
import com.nocircle.common.navigation.LocalNavController
import com.nocircle.common.navigation.NoRoute
import com.nocircle.common.navigation.popBackStack
import com.nocircle.common.windowsize.WindowWidthSize
import com.nocircle.common.windowsize.WindowWidthSizes
import com.nocircle.common.windowsize.calculateWindowWidthSize
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoTopAppBar
import com.nocircle.compose.resources.value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object AppearanceRoute : NoRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearancePage() {
	NoScaffold(
		topBar = {
			NoTopAppBar(
				title = { Text(Res.string.appearance.value()) },
				navigationIcon = {
					if (WindowWidthSizes.isCompact) {
						val controller = LocalNavController.current
						NoIconButton(
							icon = Icons.AutoMirrored.Rounded.ArrowBackIos
						) {
							controller.popBackStack()
						}
					}
				}
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
	val viewModel = koinViewModel<AppearanceViewModel>()
	val attribute by viewModel.colorSchemeAttribute.collectAsState()
	SettingsOptions(
		icon = Icons.Rounded.Contrast,
		title = Res.string.appearance_contrast.value(),
		items = ColorSchemeContrast.entries,
		current = attribute.contrast
	) { contrast ->
		val colorScheme by attribute.getColorScheme(contrast = contrast)
		val coroutineScope = rememberCoroutineScope()
		ColorSchemeCard(
			selected = attribute.contrast == contrast,
			colorScheme = colorScheme,
			name = contrast.title.value(),
			preview = Res.string.appearance_contrast_preview.value()
		) {
			if (attribute.contrast != contrast) {
				viewModel.colorSchemeAttribute.value = attribute.copy(
					contrast = contrast
				)
				coroutineScope.launch(Dispatchers.IO) {
					ColorSchemeContrastConfigKey.set(contrast.name)
				}
			}
		}
	}
}

/**
 * 主题色
 */
@Composable
private fun ColorSchemeGroupOptions() {
	val viewModel = koinViewModel<AppearanceViewModel>()
	val attribute by viewModel.colorSchemeAttribute.collectAsState()
	SettingsOptions(
		icon = Icons.Rounded.ColorLens,
		title = Res.string.appearance_theme.value(),
		items = ColorSchemeGroup.All,
		current = attribute.group
	) { group ->
		val colorScheme by attribute.getColorScheme(group)
		val coroutineScope = rememberCoroutineScope()
		ColorSchemeCard(
			selected = attribute.group == group,
			colorScheme = colorScheme,
			name = group.name.value(),
			preview = Res.string.appearance_theme_preview.value()
		) {
			if (attribute.group != group) {
				viewModel.colorSchemeAttribute.value = attribute.copy(
					group = group
				)
				coroutineScope.launch(Dispatchers.IO) {
					ColorSchemeGroupConfigKey.set(group.toString())
				}
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
	val themeModeSystemFlag by viewModel.themeModeSystemFlag.collectAsState()
	LaunchedEffect(Unit) {
		while (true) {
			themeModeSystemPercent.snapTo(0f)
			themeModeSystemPercent.animateTo(
				targetValue = 1f,
				animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing)
			)
			delay(500)
			viewModel.themeModeSystemFlag.value = !themeModeSystemFlag
		}
	}
	
	val attribute by viewModel.colorSchemeAttribute.collectAsState()
	SettingsOptions(
		icon = Icons.Rounded.DarkMode,
		title = Res.string.appearance_theme_mode.value(),
		current = attribute.themeMode,
		items = ThemeMode.entries
	) { themeMode ->
		val coroutineScope = rememberCoroutineScope()
		val onClick: () -> Unit = {
			if (attribute.themeMode != themeMode) {
				viewModel.colorSchemeAttribute.value = attribute.copy(
					themeMode = themeMode
				)
				coroutineScope.launch(Dispatchers.IO) {
					ColorSchemeThemeModeConfigKey.set(themeMode.name)
				}
			}
		}
		if (themeMode != ThemeMode.System) {
			val colorScheme by attribute.getColorScheme(themeMode = themeMode)
			ColorSchemeCard(
				selected = attribute.themeMode == themeMode,
				colorScheme = colorScheme,
				name = themeMode.title.value(),
				preview = Res.string.appearance_theme_mode_preview.value(),
				onClick = onClick
			)
		} else {
			Box(
				modifier = Modifier
					.clip(MaterialTheme.shapes.large)
					.clickable(onClick = onClick)
			) {
				val booleans by remember { mutableStateOf(arrayOf(true, false)) }
				booleans.forEach { isDark ->
					val shape by remember(themeModeSystemPercent.value) {
						derivedStateOf {
							GenericShape { size, _ ->
								moveTo(size.width * themeModeSystemPercent.value, 0f)
								lineTo(size.width * themeModeSystemPercent.value, size.height)
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
					val themeMode = ThemeMode.getThemeMode(if (themeModeSystemFlag) isDark else !isDark)
					val colorScheme by attribute.getColorScheme(themeMode = themeMode)
					ColorSchemeCard(
						selected = attribute.themeMode == ThemeMode.System,
						colorScheme = colorScheme,
						name = ThemeMode.System.title.value(),
						preview = Res.string.appearance_theme_mode_preview.value(),
						modifier = Modifier.clip(shape)
					)
				}
			}
		}
	}
}

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
	val displayCount = when (calculateWindowWidthSize()) {
		WindowWidthSize.Compact -> 2
		WindowWidthSize.Medium -> 3
		else -> 4
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
					text = if (singleLine) Res.string.appearance_multi_line.value(items.size) else Res.string.appearance_single_line.value(),
					color = MaterialTheme.colorScheme.primary,
					style = MaterialTheme.typography.bodyMedium
				)
				Spacer(Modifier.width(4.dp))
				val degrees by animateFloatAsState(
					targetValue = if (singleLine) 0f else 90f
				)
				NoIcon(
					icon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
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
					style = MaterialTheme.typography.bodyMedium
				)
				
				if (selected) {
					Spacer(Modifier.weight(1f))
					Text(
						text = Res.string.appearance_in_use.value(),
						color = MaterialTheme.colorScheme.onPrimaryContainer,
						style = MaterialTheme.typography.bodyMedium
					)
				}
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
		lazyListState.scrollToItem(items.indexOf(current), scrollOffset = scrollOffset)
	}
	LazyRow(
		modifier = Modifier
			.fillMaxWidth()
			.onGloballyPositioned {
				viewModel.colorSchemeCardWidth.value = with(density) {
					(it.size.width.toDp() - 12.dp - IntervalDp * (displayCount - 1)) / displayCount
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