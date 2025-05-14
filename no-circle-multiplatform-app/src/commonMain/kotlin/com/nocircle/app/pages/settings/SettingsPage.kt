package com.nocircle.app.pages.settings

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
import com.nocircle.app.NoNavControllerManagers
import com.nocircle.app.NoRoutes
import com.nocircle.app.constants.ColorSchemeContrastConfigKey
import com.nocircle.app.constants.ColorSchemeGroupConfigKey
import com.nocircle.app.constants.ColorSchemeThemeModeConfigKey
import com.nocircle.app.generated.resources.*
import com.nocircle.app.theme.colors.ColorSchemeContrast
import com.nocircle.app.theme.colors.ColorSchemeGroup
import com.nocircle.app.theme.colors.ThemeMode
import com.nocircle.app.theme.colors.getColorScheme
import com.nocircle.common.config.set
import com.nocircle.common.device.DeviceType
import com.nocircle.common.device.NoDevice
import com.nocircle.common.expends.value
import com.nocircle.common.windowsize.WindowWidthSize
import com.nocircle.common.windowsize.WindowWidthSizes
import com.nocircle.common.windowsize.calculateWindowWidthSize
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.material3.NoScaffold
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage() {
	NoScaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(Res.string.settings.value)
				},
				navigationIcon = {
					if (WindowWidthSizes.isCompact) {
						val navController = NoNavControllerManagers.auto(NoRoutes.Main.Person)!!
						NoIconButton(
							icon = Icons.AutoMirrored.Rounded.ArrowBackIos,
							tint = MaterialTheme.colorScheme.onSurface
						) {
							navController.popBackStack()
						}
					}
				},
				windowInsets = TopAppBarDefaults.windowInsets.add(
					WindowInsets(
						top = if (NoDevice.Type == DeviceType.Desktop && WindowWidthSizes.isCompact) 16.dp else Dp.Hairline
					)
				)
			)
		}
	) { paddingValues ->
		val verticalScrollState = rememberScrollState()
		val overscrollEffect = rememberOverscrollEffect()
		Box(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(verticalScrollState)
				.overscroll(overscrollEffect)
				.padding(paddingValues),
			contentAlignment = Alignment.TopCenter
		) {
			Column(
				modifier = Modifier
					.widthIn(max = 840.dp)
					.fillMaxSize()
					.padding(16.dp),
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
	val viewModel = koinViewModel<SettingsViewModel>()
	val currentContrast by viewModel.colorSchemeContrast.collectAsState()
	val currentGroup by viewModel.colorSchemeGroup.collectAsState()
	val themeMode by viewModel.themeMode.collectAsState()
	val isDark = themeMode.isDark
	SettingsOptions(
		icon = Icons.Rounded.Contrast,
		title = Res.string.settings_contrast.value,
		items = ColorSchemeContrast.entries,
		current = currentContrast
	) { contrast ->
		val colorScheme by remember(contrast, isDark) {
			derivedStateOf { currentGroup.getColorScheme(contrast, isDark) }
		}
		val coroutineScope = rememberCoroutineScope()
		ColorSchemeCard(
			selected = currentContrast == contrast,
			colorScheme = colorScheme,
			name = contrast.title.value,
			preview = Res.string.settings_contrast_preview.value
		) {
			viewModel.colorSchemeContrast.value = contrast
			coroutineScope.launch(Dispatchers.IO) {
				ColorSchemeContrastConfigKey.set(contrast.name)
			}
		}
	}
}

/**
 * 主题色
 */
@Composable
private fun ColorSchemeGroupOptions() {
	val viewModel = koinViewModel<SettingsViewModel>()
	val themeMode by viewModel.themeMode.collectAsState()
	val isDark = themeMode.isDark
	val currentGroup by viewModel.colorSchemeGroup.collectAsState()
	val currentContrast by viewModel.colorSchemeContrast.collectAsState()
	SettingsOptions(
		icon = Icons.Rounded.ColorLens,
		title = Res.string.settings_theme.value,
		items = ColorSchemeGroup.All,
		current = currentGroup
	) { group ->
		val colorScheme by remember(currentContrast, isDark) {
			derivedStateOf { group.getColorScheme(currentContrast, isDark) }
		}
		val coroutineScope = rememberCoroutineScope()
		ColorSchemeCard(
			selected = currentGroup == group,
			colorScheme = colorScheme,
			name = group.name.value,
			preview = Res.string.settings_theme_preview.value
		) {
			viewModel.colorSchemeGroup.value = group
			coroutineScope.launch(Dispatchers.IO) {
				ColorSchemeGroupConfigKey.set(group.toString())
			}
		}
	}
}

/**
 * 主题模式
 */
@Composable
private fun ThemeModeOptions() {
	val viewModel = koinViewModel<SettingsViewModel>()
	val currentGroup by viewModel.colorSchemeGroup.collectAsState()
	val currentContrast by viewModel.colorSchemeContrast.collectAsState()
	val currentMode by viewModel.themeMode.collectAsState()
	val themeModeSystrmPercent = viewModel.themeModeSystemPercent
	val themeModeSystemFlag by viewModel.themeModeSystemFlag.collectAsState()
	LaunchedEffect(Unit) {
		while (true) {
			themeModeSystrmPercent.animateTo(
				targetValue = 0f,
				animationSpec = snap()
			)
			themeModeSystrmPercent.animateTo(
				targetValue = 1f,
				animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing)
			)
			delay(500)
			viewModel.themeModeSystemFlag.value = !themeModeSystemFlag
		}
	}
	
	SettingsOptions(
		icon = Icons.Rounded.DarkMode,
		title = Res.string.settings_theme_mode.value,
		current = currentMode,
		items = ThemeMode.entries
	) { mode ->
		val coroutineScope = rememberCoroutineScope()
		if (mode != ThemeMode.System) {
			val isDark = mode.isDark
			val colorScheme by remember(currentContrast, isDark) {
				derivedStateOf { currentGroup.getColorScheme(currentContrast, isDark) }
			}
			ColorSchemeCard(
				selected = currentMode == mode,
				colorScheme = colorScheme,
				name = mode.title.value,
				preview = Res.string.settings_theme_mode_preview.value
			) {
				viewModel.themeMode.value = mode
				coroutineScope.launch(Dispatchers.IO) {
					ColorSchemeThemeModeConfigKey.set(mode.name)
				}
			}
		} else {
			Box(
				modifier = Modifier
					.clip(MaterialTheme.shapes.large)
					.clickable {
						viewModel.themeMode.value = mode
						coroutineScope.launch(Dispatchers.IO) {
							ColorSchemeThemeModeConfigKey.set(mode.name)
						}
					}
			) {
				var booleans by remember { mutableStateOf(arrayOf(true, false)) }
				booleans.forEach { isDark ->
					val shape by remember(themeModeSystrmPercent.value) {
						derivedStateOf {
							GenericShape { size, _ ->
								moveTo(size.width * themeModeSystrmPercent.value, 0f)
								lineTo(size.width * themeModeSystrmPercent.value, size.height)
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
					val isDark = if (themeModeSystemFlag) isDark else !isDark
					val colorScheme by remember(currentContrast, isDark) {
						derivedStateOf { currentGroup.getColorScheme(currentContrast, isDark) }
					}
					ColorSchemeCard(
						selected = currentMode == mode,
						colorScheme = colorScheme,
						name = mode.title.value,
						preview = Res.string.settings_theme_mode_preview.value,
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
					text = if (singleLine) "显示全部" else "单行显示",
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
						text = Res.string.settings_in_use.value,
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
	val viewModel = koinViewModel<SettingsViewModel>()
	val cardWidth by viewModel.colorSchemeCardWidth.collectAsState()
	val lazyListState = rememberLazyListState()
	LaunchedEffect(Unit) {
		lazyListState.scrollToItem(items.indexOf(current))
	}
	LazyRow(
		modifier = Modifier
			.fillMaxWidth()
			.onGloballyPositioned {
				viewModel.colorSchemeCardWidth.value = with(density) {
					(it.size.width.toDp() - IntervalDp * (displayCount - 1)) / displayCount
				}
			},
		state = lazyListState
	) {
		itemsIndexed(items) { index, item ->
			if (cardWidth == Dp.Unspecified) return@itemsIndexed
			Box(
				modifier = Modifier
					.width(cardWidth)
			) {
				content(item)
			}
			if (index < items.lastIndex) {
				Spacer(modifier = Modifier.width(IntervalDp))
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
	val viewModel = koinViewModel<SettingsViewModel>()
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
			}
			if (index < allItems.lastIndex) {
				Spacer(modifier = Modifier.height(IntervalDp))
			}
		}
	}
}