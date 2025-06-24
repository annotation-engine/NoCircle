package com.nocircle.app.pages.settings

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.nocircle.app.pages.settings.appearance.AppearanceRoute
import com.nocircle.app.pages.settings.memory.Memory
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.app.theme.type.FontWeightType
import com.nocircle.app.theme.type.RoundedCornerType
import com.nocircle.compose.foundation.NoButton
import com.nocircle.compose.foundation.NoButtonColors
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.layout.NoAlertModalBottomSheet
import com.nocircle.compose.layout.NoOption
import com.nocircle.compose.layout.VerticalScrollColumn
import com.nocircle.compose.material3.NoDropdownMenu
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoTopAppBar
import com.nocircle.compose.navigation.LocalNavController
import com.nocircle.compose.navigation.NoRoute
import com.nocircle.compose.resources.NoIconType
import com.nocircle.compose.resources.SupportedLanguage
import com.nocircle.compose.resources.value
import com.nocircle.compose.windowsize.WindowWidthSizes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object SettingsRoute : NoRoute

@Composable
fun SettingsPage() {
	val controller = LocalNavController.current
	NoScaffold(
		topBar = {
			NoTopAppBar(
				title = { Text(AppString.SETTINGS.value()) },
				navigationIcon = if (WindowWidthSizes.isCompact) {
					{
						NoIconButton(
							icon = AppIcon.ArrowBack.value()
						) {
							controller.popBackStack()
						}
					}
				} else null
			)
		},
	) { paddingValues ->
		VerticalScrollColumn(
			modifier = Modifier
				.padding(paddingValues)
		) {
			val viewModel = koinViewModel<SettingsViewModel>()
			NavToAppearance()
			Spacer(modifier = Modifier.height(16.dp))
			SwitchLanguage()
			Spacer(modifier = Modifier.height(16.dp))
			SwitchIconType()
			Spacer(modifier = Modifier.height(16.dp))
			SwitchShapesType()
			Spacer(modifier = Modifier.height(16.dp))
			SwitchFontWeight()
			Spacer(modifier = Modifier.height(16.dp))
			Memory()
			Spacer(modifier = Modifier.height(16.dp))
			Logout(viewModel)
		}
	}
}

/**
 * 前往外观页
 */
@Composable
private fun NavToAppearance() {
	val controller = LocalNavController.current
	NoOption(
		title = { Text(AppString.APPEARANCE.value()) },
		icon = { NoIcon(AppIcon.Cookie.value()) },
		actions = {
			Text(
				text = AppString.SETTINGS_APPEARANCE_SUBTITLE.value(),
				overflow = TextOverflow.Ellipsis,
				maxLines = 1
			)
		}
	) {
		controller.navigate(AppearanceRoute) {
			launchSingleTop = true
		}
	}
}

/**
 * 切换语言
 */
@Composable
private fun SwitchLanguage() {
	val language = SupportedLanguage.current
	SupportedLanguage
	var expanded by remember { mutableStateOf(false) }
	NoDropdownMenu(
		expanded = expanded,
		onExpandedChange = { expanded = it },
		menuItems = {
			val coroutineScope = rememberCoroutineScope()
			SupportedLanguage.entries.fastForEach {
				DropdownMenuItem(
					text = {
						Text(it.displayName)
					},
					onClick = {
						coroutineScope.launch(Dispatchers.IO) {
							SupportedLanguage.update(it)
						}
						expanded = false
					}
				)
			}
		}
	) {
		NoOption(
			title = { Text(AppString.SETTINGS_SWITCH_LANGUAGE.value()) },
			icon = { NoIcon(AppIcon.Language.value()) },
			actions = {
				Text(
					text = language.displayName,
					overflow = TextOverflow.Ellipsis,
					maxLines = 1
				)
			},
		)
	}
}

/**
 * 切换图标类型
 */
@Composable
private fun SwitchIconType() {
	var expanded by remember { mutableStateOf(false) }
	NoDropdownMenu(
		expanded = expanded,
		onExpandedChange = { expanded = it },
		menuItems = {
			val coroutineScope = rememberCoroutineScope()
			NoIconType.entries.fastForEach {
				DropdownMenuItem(
					text = { Text(it.getAppString().value()) },
					onClick = {
						coroutineScope.launch(Dispatchers.IO) {
							NoIconType.update(it)
						}
						expanded = false
					}
				)
			}
		}
	) {
		NoOption(
			title = { Text(AppString.SETTINGS_SWITCH_ICON_TYPE.value()) },
			icon = { NoIcon(AppIcon.ShapeLine.value()) },
			actions = {
				Text(
					text = NoIconType.current.getAppString().value(),
					overflow = TextOverflow.Ellipsis,
					maxLines = 1
				)
			}
		)
	}
}

@Stable
private fun NoIconType.getAppString(): AppString = when (this) {
	NoIconType.ROUNDED -> AppString.SETTINGS_ROUNDED
	NoIconType.OUTLINED -> AppString.SETTINGS_OUTLINED
	NoIconType.FILLED -> AppString.SETTINGS_FILLED
	NoIconType.SHARP -> AppString.SETTINGS_SHARP
	NoIconType.TWO_TONE -> AppString.SETTINGS_TWO_TONE
}

/**
 * 切换圆角类型
 */
@Composable
private fun SwitchShapesType() {
	var expanded by remember { mutableStateOf(false) }
	NoDropdownMenu(
		expanded = expanded,
		onExpandedChange = { expanded = it },
		menuItems = {
			val coroutineScope = rememberCoroutineScope()
			RoundedCornerType.entries.fastForEach {
				DropdownMenuItem(
					text = { Text(it.getAppString().value()) },
					onClick = {
						coroutineScope.launch(Dispatchers.IO) {
							RoundedCornerType.update(it)
						}
						expanded = false
					}
				)
			}
		}
	) {
		NoOption(
			title = { Text(AppString.SETTINGS_ROUNDED_CORNER_TYPE.value()) },
			icon = { NoIcon(AppIcon.RoundedCorner.value()) },
			actions = {
				Text(
					text = RoundedCornerType.current.getAppString().value(),
					overflow = TextOverflow.Ellipsis,
					maxLines = 1
				)
			}
		)
	}
}

@Stable
private fun RoundedCornerType.getAppString(): AppString = when (this) {
	RoundedCornerType.EXTRA_SMALL -> AppString.SETTINGS_ROUNDED_CORNER_EXTRA_SMALL
	RoundedCornerType.SMALL -> AppString.SETTINGS_ROUNDED_CORNER_SMALL
	RoundedCornerType.NORMAL -> AppString.SETTINGS_ROUNDED_CORNER_NORMAL
	RoundedCornerType.LARGE -> AppString.SETTINGS_ROUNDED_CORNER_LARGE
	RoundedCornerType.EXTRA_LARGE -> AppString.SETTINGS_ROUNDED_CORNER_EXTRA_LARGE
}

@Composable
private fun SwitchFontWeight() {
	var expanded by remember { mutableStateOf(false) }
	NoDropdownMenu(
		expanded = expanded,
		onExpandedChange = { expanded = it },
		menuItems = {
			val coroutineScope = rememberCoroutineScope()
			FontWeightType.entries.fastForEach {
				DropdownMenuItem(
					text = { Text(it.getAppString().value()) },
					onClick = {
						coroutineScope.launch(Dispatchers.IO) {
							FontWeightType.update(it)
						}
						expanded = false
					}
				)
			}
		}
	) {
		NoOption(
			title = { Text(AppString.SETTINGS_FONT_WEIGHT_TYPE.value()) },
			icon = { NoIcon(AppIcon.LineWeight.value()) },
			actions = {
				Text(
					text = FontWeightType.current.getAppString().value(),
					overflow = TextOverflow.Ellipsis,
					maxLines = 1
				)
			}
		)
	}
}

@Stable
private fun FontWeightType.getAppString(): AppString = when (this) {
	FontWeightType.EXTRA_LIGHT -> AppString.SETTINGS_FONT_WEIGHT_EXTRA_LIGHT
	FontWeightType.LIGHT -> AppString.SETTINGS_FONT_WEIGHT_LIGHT
	FontWeightType.NORMAL -> AppString.SETTINGS_FONT_WEIGHT_NORMAL
	FontWeightType.BOLD -> AppString.SETTINGS_FONT_WEIGHT_BOLD
	FontWeightType.EXTRA_BOLD -> AppString.SETTINGS_FONT_WEIGHT_EXTRA_BOLD
}

/**
 * 退出登录
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Logout(
	viewModel: SettingsViewModel
) {
	var showLogoutModal by remember { mutableStateOf(false) }
	if (showLogoutModal) {
		NoAlertModalBottomSheet(
			title = {
				Text(AppString.SETTINGS_LOGOUT_TITLE.value())
			},
			content = {
				Text(AppString.SETTINGS_LOGOUT_CONTENT.value())
			},
			onDismissRequest = { showLogoutModal = false },
			onConfirm = {
				viewModel.logout()
			},
			confirmColors = NoButtonColors.ErrorColors,
			icon = {
				NoIcon(
					icon = AppIcon.Warning.value(),
					tint = MaterialTheme.colorScheme.error
				)
			}
		)
	}
	NoButton(
		text = AppString.SETTINGS_LOGOUT.value(),
		modifier = Modifier
			.fillMaxWidth()
			.height(60.dp),
		colors = NoButtonColors.ErrorColors
	) {
		showLogoutModal = true
	}
}