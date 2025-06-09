package com.nocircle.app.pages.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.nocircle.app.pages.account.login.LoginRoute
import com.nocircle.app.pages.settings.appearance.AppearanceRoute
import com.nocircle.app.pages.settings.memory.Memory
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.app.rootController
import com.nocircle.common.navigation.LocalNavController
import com.nocircle.common.navigation.NoPopUp
import com.nocircle.common.navigation.NoRoute
import com.nocircle.common.resources.IconType
import com.nocircle.common.resources.SupportedLanguage
import com.nocircle.common.resources.value
import com.nocircle.common.windowsize.WindowWidthSizes
import com.nocircle.compose.foundation.NoButtonColors
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.layout.NoAlertModalBottomSheet
import com.nocircle.compose.layout.NoOption
import com.nocircle.compose.material3.NoDropdownMenu
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoTopAppBar
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
				title = { Text(AppString.Settings.value()) },
				onNavigationIconClick = if (WindowWidthSizes.isCompact) {
					{ controller.popBackStack() }
				} else null
			)
		},
	) { paddingValues ->
		val verticalScrollState = rememberScrollState()
		Box(
			modifier = Modifier
				.fillMaxSize()
				.padding(top = paddingValues.calculateTopPadding())
				.verticalScroll(verticalScrollState),
			contentAlignment = Alignment.TopCenter
		) {
			Column(
				modifier = Modifier
					.widthIn(max = 840.dp)
					.fillMaxSize()
					.padding(
						horizontal = 16.dp,
						vertical = 32.dp
					)
			) {
				val viewModel = koinViewModel<SettingsViewModel>()
				NavToAppearance()
				Spacer(modifier = Modifier.height(16.dp))
				SwitchLanguage()
				Spacer(modifier = Modifier.height(16.dp))
				SwitchIconType()
				Spacer(modifier = Modifier.height(16.dp))
				Memory()
				Spacer(modifier = Modifier.height(16.dp))
				Logout(viewModel)
			}
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
		title = { Text(AppString.Appearance.value()) },
		icon = { NoIcon(AppIcon.Cookie.value()) },
		actions = { Text(AppString.SettingsAppearanceSubtitle.value()) }
	) {
		controller.navigate(route = AppearanceRoute)
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
							SupportedLanguage.set(it)
						}
						expanded = false
					}
				)
			}
		}
	) {
		NoOption(
			title = { Text(AppString.SettingsSwitchLanguage.value()) },
			icon = { NoIcon(AppIcon.Language.value()) },
			actions = { Text(language.displayName) },
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
			IconType.entries.fastForEach {
				DropdownMenuItem(
					text = { Text(it.getAppString().value()) },
					onClick = {
						coroutineScope.launch(Dispatchers.IO) {
							IconType.set(it)
						}
						expanded = false
					}
				)
			}
		}
	) {
		NoOption(
			title = { Text(AppString.SettingsSwitchIconType.value()) },
			icon = { NoIcon(AppIcon.ShapeLine.value()) },
			actions = { Text(IconType.current.getAppString().value()) }
		)
	}
}

@Stable
private fun IconType.getAppString(): AppString = when (this) {
	IconType.Rounded -> AppString.SettingsRounded
	IconType.Outlined -> AppString.SettingsOutlined
	IconType.Filled -> AppString.SettingsFilled
	IconType.Sharp -> AppString.SettingsSharp
	IconType.TwoTone -> AppString.SettingsTwoTone
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
				Text(AppString.SettingsLogoutTitle.value())
			},
			content = {
				Text(AppString.SettingsLogoutContent.value())
			},
			onDismissRequest = { showLogoutModal = false },
			onConfirm = {
				viewModel.logout()
				rootController?.navigate(
					route = LoginRoute,
					popup = NoPopUp.All
				)
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
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.height(60.dp)
			.clip(MaterialTheme.shapes.small)
			.background(
				color = MaterialTheme.colorScheme.error
			)
			.clickable {
				showLogoutModal = true
			},
		contentAlignment = Alignment.Center
	) {
		Text(
			text = AppString.SettingsLogout.value(),
			style = MaterialTheme.typography.bodyLarge,
			color = MaterialTheme.colorScheme.onError
		)
	}
}