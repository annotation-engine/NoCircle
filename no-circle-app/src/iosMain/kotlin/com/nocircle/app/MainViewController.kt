package com.nocircle.app

import androidx.compose.ui.window.ComposeUIViewController
import com.nocircle.app.theme.NoMaterialTheme

@Suppress("unused", "FunctionName")
fun MainViewController() = ComposeUIViewController {
	NavControllerProvider {
		NoMaterialTheme {
			NoApp()
		}
	}
}