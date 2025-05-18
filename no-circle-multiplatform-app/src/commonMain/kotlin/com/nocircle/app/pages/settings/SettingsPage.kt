package com.nocircle.app.pages.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nocircle.app.NoNavControllerManagers
import com.nocircle.app.NoNavHost
import com.nocircle.app.NoRoutes
import com.nocircle.app.generated.resources.Res
import com.nocircle.app.generated.resources.appearance
import com.nocircle.app.generated.resources.settings
import com.nocircle.common.device.DeviceType
import com.nocircle.common.device.NoDevice
import com.nocircle.common.expends.value
import com.nocircle.common.windowsize.WindowWidthSizes
import com.nocircle.compose.compose.NoOption
import com.nocircle.compose.foundation.NoIconButton
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoTopAppBar
import com.nocircle.compose.material3.NoTopAppBarDefaults

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
					icon = Icons.Rounded.Apps
				) {
					navController.navigate(NoRoutes.Settings.Appearance)
				}
			}
		}
	}
}