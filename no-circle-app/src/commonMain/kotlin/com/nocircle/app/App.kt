package com.nocircle.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nocircle.app.page.AccountPage
import com.nocircle.app.theme.NoMaterialTheme

@Composable
fun App() {
	NoMaterialTheme {
		val navController = rememberNavController()
		NavHost(navController, AppRoute.ACCOUNT) {
			composable(AppRoute.ACCOUNT) {
				AccountPage(navController)
			}
		}
	}
}

data object AppRoute {
	
	const val ACCOUNT = "account"
}