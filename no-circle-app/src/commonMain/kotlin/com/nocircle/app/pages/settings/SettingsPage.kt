package com.nocircle.app.pages.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.nocircle.app.LocalNavController
import com.nocircle.common.expends.noPopBackStack
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
					Text("Settings")
				},
				navigationIcon = {
					val navController = LocalNavController.current
					NoIcon(
						icon = Icons.AutoMirrored.Rounded.ArrowBackIos,
						tint = MaterialTheme.colorScheme.onSurface
					) {
						navController.noPopBackStack()
					}
				}
			)
		}
	) {
	
	}
}