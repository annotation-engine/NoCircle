package com.nocircle.app.pages.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Contrast
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nocircle.app.NoNavControllerManagers
import com.nocircle.app.NoRoutes
import com.nocircle.app.generated.resources.*
import com.nocircle.app.theme.colors.ColorSchemeContrast
import com.nocircle.app.theme.colors.getColorScheme
import com.nocircle.common.device.DeviceType
import com.nocircle.common.device.NoDevice
import com.nocircle.common.expends.value
import com.nocircle.common.windowsize.WindowWidthSizes
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.material3.NoScaffold
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
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues),
			contentAlignment = Alignment.TopCenter
		) {
			Column(
				modifier = Modifier
					.widthIn(max = 700.dp)
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
					ColorSchemeContrast()
				}
				Spacer(Modifier.height(16.dp))
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
}

@Composable
private fun ColorSchemeContrast() {
	val viewModel = koinViewModel<SettingsViewModel>()
	Row(
		modifier = Modifier
			.fillMaxWidth()
	) {
		val group by viewModel.colorSchemeGroup.collectAsState()
		val contrast by viewModel.colorSchemeContrast.collectAsState()
		val darkTheme by viewModel.darkTheme.collectAsState()
		val currentColorScheme = group.getColorScheme(contrast, darkTheme)
		val colorSchemes by remember(group, darkTheme) {
			derivedStateOf {
				if (darkTheme) {
					arrayOf(group.darkStandardContrast, group.darkMediumContrast, group.darkHighContrast)
				} else {
					arrayOf(group.lightStandardContrast, group.lightMediumContrast, group.lightHighContrast)
				}
			}
		}
		val contrasts = remember {
			arrayOf(
				Res.string.settings_contrast_standard,
				Res.string.settings_contrast_medium,
				Res.string.settings_contrast_high,
			)
		}
		colorSchemes.forEachIndexed { index, colorScheme ->
			MaterialTheme(
				colorScheme = colorScheme
			) {
				Column(
					modifier = Modifier
						.weight(1f)
						.clip(MaterialTheme.shapes.medium)
						.background(
							color = MaterialTheme.colorScheme.primaryContainer,
							shape = MaterialTheme.shapes.medium
						)
						.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
						) {
							viewModel.colorSchemeContrast.value = ColorSchemeContrast.entries[index]
						}
						.padding(4.dp),
					horizontalAlignment = Alignment.CenterHorizontally,
				) {
					Row(
						modifier = Modifier
							.fillMaxWidth()
							.height(80.dp)
							.clip(MaterialTheme.shapes.small)
					) {
						val colors by remember(colorScheme) {
							derivedStateOf {
								arrayOf(
									colorScheme.primary,
									colorScheme.primaryContainer,
									colorScheme.secondary,
									colorScheme.secondaryContainer,
									colorScheme.tertiary,
									colorScheme.tertiaryContainer,
									colorScheme.inverseSurface,
									colorScheme.surface,
									colorScheme.error,
									colorScheme.errorContainer
								)
							}
						}
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
											text = "${row * 5 + col}",
											color = contentColorFor(backgroundColor),
											style = MaterialTheme.typography.bodyLarge
										)
									}
								}
							}
						}
					}
					
					Spacer(Modifier.height(4.dp))
					
					Row(
						verticalAlignment = Alignment.CenterVertically,
					) {
						RadioButton(
							selected = currentColorScheme == colorScheme,
							onClick = {
								viewModel.colorSchemeContrast.value = ColorSchemeContrast.entries[index]
							},
							colors = RadioButtonDefaults.colors(
								selectedColor = MaterialTheme.colorScheme.onPrimaryContainer,
								unselectedColor = MaterialTheme.colorScheme.onPrimaryContainer
							)
						)
						Text(
							text = contrasts[index].value,
							color = MaterialTheme.colorScheme.onPrimaryContainer,
							style = MaterialTheme.typography.bodyMedium
						)
					}
					
				}
			}
			if (index < colorSchemes.lastIndex) {
				Spacer(Modifier.width(16.dp))
			}
		}
	}
}