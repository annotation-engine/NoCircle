package com.nocircle.app.pages.settings

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nocircle.app.NoNavControllerManagers
import com.nocircle.app.NoRoutes
import com.nocircle.app.generated.resources.Res
import com.nocircle.app.generated.resources.settings
import com.nocircle.common.device.DeviceType
import com.nocircle.common.device.NoDevice
import com.nocircle.common.expends.WindowWidthSizes
import com.nocircle.common.expends.value
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.material3.NoScaffold
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage() {
	val viewModel = koinViewModel<SettingsViewModel>()
	NoScaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(Res.string.settings.value)
				},
				navigationIcon = {
					if (WindowWidthSizes.isCompact) {
						val navController = NoNavControllerManagers.auto(NoRoutes.Main.Person)!!
						NoIcon(
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
	) {
	
	}
}