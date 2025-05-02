package com.nocircle.app.pages.main

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.nocircle.app.generated.resources.Res
import com.nocircle.app.generated.resources.register_success
import com.nocircle.common.material3.showNoSnackbar
import com.nocircle.compose.material3.NoScaffold
import com.nocircle.compose.material3.NoSnackbar
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainPage(
	navController: NavController,
	viewModel: MainViewModel = koinViewModel()
) {
	val hostState = remember { SnackbarHostState() }
	LaunchedEffect(Unit) {
		hostState.showNoSnackbar(Res.string.register_success)
		viewModel.snackbarCollect(hostState::showNoSnackbar)
	}
	NoScaffold(
		snackbarHost = {
			SnackbarHost(hostState) {
				NoSnackbar(it)
			}
		}
	) {
	
	}
}