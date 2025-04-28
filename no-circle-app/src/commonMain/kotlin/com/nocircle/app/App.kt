package com.nocircle.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nocircle.app.page.account.login.LoginPage
import com.nocircle.app.page.account.register.RegisterPage
import com.nocircle.app.page.main.MainPage
import com.nocircle.app.theme.NoMaterialTheme

@Composable
fun App() {
	NoMaterialTheme {
		val navController = rememberNavController()
		NavHost(navController, AppRoute.ACCOUNT_LOGIN) {
			composable(AppRoute.ACCOUNT_LOGIN) {
				println(it.destination.route?.toString())
				LoginPage(navController)
			}
			
			composable(AppRoute.ACCOUNT_REGISTER) {
				RegisterPage(navController)
			}
			
			composable(AppRoute.MAIN) {
				MainPage(navController)
			}
		}
	}
}

data object AppRoute {
	
	const val ACCOUNT_LOGIN = "/account/login"
	
	const val ACCOUNT_REGISTER = "/account/register"
	
	const val MAIN = "/main"
}