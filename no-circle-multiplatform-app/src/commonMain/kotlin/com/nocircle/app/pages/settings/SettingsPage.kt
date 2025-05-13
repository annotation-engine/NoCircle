package com.nocircle.app.pages.settings

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.Contrast
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nocircle.app.NoNavControllerManagers
import com.nocircle.app.NoRoutes
import com.nocircle.app.constants.ColorSchemeContrastConfigKey
import com.nocircle.app.constants.ColorSchemeGroupConfigKey
import com.nocircle.app.generated.resources.*
import com.nocircle.app.theme.colors.ColorSchemeContrast
import com.nocircle.app.theme.colors.ColorSchemeGroup
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
					.widthIn(max = 900.dp)
					.fillMaxSize()
					.padding(
						horizontal = 16.dp,
						vertical = 32.dp
					),
			) {
				SettingsCard(
					icon = Icons.Rounded.Contrast,
					title = Res.string.settings_contrast.value
				) {
					ColorSchemeContrastOptions()
				}
				SettingsCard(
					icon = Icons.Rounded.ColorLens,
					title = Res.string.settings_theme.value
				) {
					ColorSchemeGroupOptions()
				}
			}
		}
	}
}

@Composable
private fun SettingsCard(
	icon: ImageVector,
	title: String,
	content: @Composable () -> Unit
) {
	Row(
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
			style = MaterialTheme.typography.titleLarge
		)
	}
	Spacer(Modifier.height(16.dp))
	content()
	Spacer(Modifier.height(32.dp))
}

@Composable
private fun ColorSchemeContrastOptions() {
	val viewModel = koinViewModel<SettingsViewModel>()
	val currentContrast by viewModel.colorSchemeContrast.collectAsState()
	val currentGroup by viewModel.colorSchemeGroup.collectAsState()
	val themeMode by viewModel.themeMode.collectAsState()
	val isDark = themeMode.isDark
	Row(
		modifier = Modifier
			.fillMaxWidth()
	) {
		val coroutineScope = rememberCoroutineScope()
		ColorSchemeContrast.entries.forEachIndexed { index, contrast ->
			val colorScheme by remember(contrast, isDark) {
				derivedStateOf { currentGroup.getColorScheme(contrast, isDark) }
			}
			ColorSchemeCard(
				selected = currentContrast == contrast,
				onClick = {
					viewModel.colorSchemeContrast.value = contrast
					coroutineScope.launch(Dispatchers.IO) {
						ColorSchemeContrastConfigKey.set(contrast.name)
					}
				},
				colorScheme = colorScheme,
				name = contrast.title.value,
				preview = Res.string.settings_contrast_preview.value
			)
			if (index < ColorSchemeContrast.entries.lastIndex) {
				Spacer(modifier = Modifier.width(16.dp))
			}
		}
	}
}

@Composable
private fun ColorSchemeGroupOptions() {
	val viewModel = koinViewModel<SettingsViewModel>()
	val themeMode by viewModel.themeMode.collectAsState()
	val currentGroup by viewModel.colorSchemeGroup.collectAsState()
	val currentContrast by viewModel.colorSchemeContrast.collectAsState()
	val size = when (calculateWindowWidthSize()) {
		WindowWidthSize.Compact -> 2
		WindowWidthSize.Medium -> 3
		else -> 4
	}
	val allGroups = remember(size) {
		ColorSchemeGroup.All.chunked(size)
	}
	val isDark = themeMode.isDark
	val coroutineScope = rememberCoroutineScope()
	allGroups.forEachIndexed { index, groups ->
		Row(
			modifier = Modifier
				.fillMaxWidth()
		) {
			groups.forEachIndexed { index, group ->
				val colorScheme by remember(currentContrast, isDark) {
					derivedStateOf { group.getColorScheme(currentContrast, isDark) }
				}
				ColorSchemeCard(
					selected = currentGroup == group,
					onClick = {
						viewModel.colorSchemeGroup.value = group
						coroutineScope.launch(Dispatchers.IO) {
							ColorSchemeGroupConfigKey.set(group.toString())
						}
					},
					colorScheme = colorScheme,
					name = group.name.value,
					preview = Res.string.settings_theme_preview.value
				)
				if (index < groups.lastIndex) {
					Spacer(modifier = Modifier.width(16.dp))
				}
			}
			repeat(size - groups.size) {
				Spacer(modifier = Modifier.width(16.dp))
				Spacer(modifier = Modifier.weight(1f))
			}
		}
		if (index < allGroups.lastIndex) {
			Spacer(modifier = Modifier.height(16.dp))
		}
	}
}

@Composable
private fun RowScope.ColorSchemeCard(
	selected: Boolean,
	onClick: () -> Unit,
	colorScheme: ColorScheme,
	name: String,
	preview: String,
) {
	MaterialTheme(colorScheme) {
		var scaleTarget by remember { mutableStateOf(1f) }
		val scale by animateFloatAsState(scaleTarget)
		val interactionSource = remember { MutableInteractionSource() }
		val isHovered by interactionSource.collectIsHoveredAsState()
		val isPressed by interactionSource.collectIsPressedAsState()
		LaunchedEffect(isHovered, isPressed) {
			scaleTarget = when {
				isPressed -> 0.98f
				isHovered -> 1.02f
				else -> 1f
			}
		}
		Column(
			modifier = Modifier
				.scale(scale)
				.weight(1f)
				.clip(MaterialTheme.shapes.large)
				.background(
					color = MaterialTheme.colorScheme.primaryContainer,
					shape = MaterialTheme.shapes.large
				)
				.hoverable(interactionSource)
				.clickable(
					interactionSource = interactionSource,
					indication = LocalIndication.current,
					onClick = onClick
				)
				.padding(4.dp)
		) {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.height(80.dp)
					.clip(MaterialTheme.shapes.medium)
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