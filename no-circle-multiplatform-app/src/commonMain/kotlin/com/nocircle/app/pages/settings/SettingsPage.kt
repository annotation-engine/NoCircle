package com.nocircle.app.pages.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Cookie
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.nocircle.app.generated.resources.*
import com.nocircle.app.pages.account.login.LoginRoute
import com.nocircle.app.pages.main.person.PersonNavHostKey
import com.nocircle.app.pages.settings.appearance.AppearanceRoute
import com.nocircle.common.navigation.NoNavControllerManager
import com.nocircle.common.navigation.NoPopUp
import com.nocircle.common.navigation.NoRoute
import com.nocircle.common.windowsize.WindowWidthSizes
import com.nocircle.compose.compose.NoAlertModalBottomSheet
import com.nocircle.compose.compose.NoOption
import com.nocircle.compose.foundation.NoButtons
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.foundation.layout.autoPadding
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoTopAppBar
import com.nocircle.compose.resources.value
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
data object SettingsRoute : NoRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage() {
	val navController = NoNavControllerManager.get(PersonNavHostKey)
	NoScaffold(
		topBar = {
			NoTopAppBar(
				title = { Text(Res.string.settings.value()) },
				navigationIcon = {
					if (WindowWidthSizes.isCompact) {
						NoIconButton(
							icon = Icons.AutoMirrored.Rounded.ArrowBackIos
						) {
							navController.popBackStack()
						}
					}
				}
			)
		},
	) { paddingValues ->
		val verticalScrollState = rememberScrollState()
		Box(
			modifier = Modifier
				.fillMaxSize()
				.autoPadding(paddingValues)
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
				NoOption(
					title = Res.string.appearance.value(),
					subtitle = Res.string.settings_appearance_subtitle.value(),
					icon = Icons.Rounded.Cookie
				) {
					navController.navigate(AppearanceRoute)
				}
				Spacer(Modifier.height(16.dp))
				Logout()
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Logout() {
	var showLogoutModal by remember { mutableStateOf(false) }
	if (showLogoutModal) {
		val viewModel = koinViewModel<SettingsViewModel>()
		val controller = NoNavControllerManager.get()
		NoAlertModalBottomSheet(
			title = {
				NoIcon(
					icon = Icons.Rounded.Warning,
					tint = MaterialTheme.colorScheme.error
				)
				Spacer(Modifier.width(8.dp))
				Text(
					text = Res.string.settings_logout_title.value(),
				)
			},
			content = {
				Text(
					text = Res.string.settings_logout_content.value(),
				)
			},
			onDismissRequest = { showLogoutModal = false },
			onConfirm = {
				val success = viewModel.logout()
				if (success) {
					controller.navigate(LoginRoute, popup = NoPopUp.All)
				}
			},
			confirmColors = NoButtons.ErrorColors
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
			text = Res.string.settings_logout.value(),
			style = MaterialTheme.typography.bodyLarge,
			color = MaterialTheme.colorScheme.onError
		)
	}
}