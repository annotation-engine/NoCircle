package com.nocircle.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nocircle.app.page.account.login.LoginPage
import com.nocircle.app.page.account.register.RegisterPage
import com.nocircle.app.page.guide.GuidePage
import com.nocircle.app.page.main.MainPage
import com.nocircle.app.theme.NoMaterialTheme

@Composable
fun App() {
	NoMaterialTheme {
		val navController = rememberNavController()
		
		NavHost(navController, NoRoute.GUIDE) {
			composable(NoRoute.GUIDE) {
				GuidePage(navController)
			}
			
			composable(NoRoute.ACCOUNT_LOGIN) {
				LoginPage(navController)
			}
			
			composable(NoRoute.ACCOUNT_REGISTER) {
				RegisterPage(navController)
			}
			
			composable(NoRoute.MAIN) {
				MainPage(navController)
			}
		}
	}
}

object NoRoute {
	
	const val GUIDE = "/guide"
	
	const val ACCOUNT_LOGIN = "/account/login"
	
	const val ACCOUNT_REGISTER = "/account/register"
	
	const val MAIN = "/main"
}