package com.nocircle.app.pages.settings.items

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nocircle.app.pages.settings.SettingsViewModel
import com.nocircle.app.resources.AppIcon
import com.nocircle.app.resources.AppString
import com.nocircle.compose.foundation.NoButton
import com.nocircle.compose.foundation.NoButtonColors
import com.nocircle.compose.foundation.NoIcon
import com.nocircle.compose.layout.NoAlertModalBottomSheet
import com.nocircle.compose.resources.value
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Logout() {
	val viewModel = koinViewModel<SettingsViewModel>()
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