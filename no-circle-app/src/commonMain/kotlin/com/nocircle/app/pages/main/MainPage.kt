package com.nocircle.app.pages.main

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.nocircle.compose.material3.NoScaffold

@Composable
fun MainPage(
	navController: NavController,
	hostState: SnackbarHostState = remember { SnackbarHostState() },
	viewModel: MainViewModel = remember { MainViewModel() }
) {
	NoScaffold {
	
	}
}