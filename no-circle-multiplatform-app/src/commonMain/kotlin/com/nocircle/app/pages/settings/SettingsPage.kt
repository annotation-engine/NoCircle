package com.nocircle.app.pages.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Cookie
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nocircle.app.NoNavControllerManagers
import com.nocircle.app.NoNavHost
import com.nocircle.app.generated.resources.Res
import com.nocircle.app.generated.resources.appearance
import com.nocircle.app.generated.resources.logout
import com.nocircle.app.generated.resources.settings
import com.nocircle.app.pages.settings.appearance.AppearanceRoute
import com.nocircle.common.device.DeviceType
import com.nocircle.common.device.NoDevice
import com.nocircle.common.expends.value
import com.nocircle.common.navigation.NoRoute
import com.nocircle.common.windowsize.WindowWidthSizes
import com.nocircle.compose.compose.NoOption
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.material3.NoModalBottomSheet
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoTopAppBar
import com.nocircle.compose.material3.NoTopAppBarDefaults
import kotlinx.serialization.Serializable

@Serializable
data object SettingsRoute : NoRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage() {
	val navController = NoNavControllerManagers.get(NoNavHost.Person)
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
				},
				windowInsets = NoTopAppBarDefaults.windowInsets.add(
					insets = WindowInsets(
						top = if (NoDevice.Type == DeviceType.Desktop && WindowWidthSizes.isCompact) 16.dp else Dp.Hairline
					)
				)
			)
		},
	) { paddingValues ->
		val verticalScrollState = rememberScrollState()
		Box(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(verticalScrollState)
				.padding(paddingValues),
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

@Composable
private fun Logout() {
	var showLogoutModal by remember { mutableStateOf(false) }
	if (showLogoutModal) {
		NoModalBottomSheet(
			onDismissRequest = { showLogoutModal = false },
		) {
		
		}
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
			text = Res.string.logout.value(),
			style = MaterialTheme.typography.bodyLarge,
			color = MaterialTheme.colorScheme.onError
		)
	}
}