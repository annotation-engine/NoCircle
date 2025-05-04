package com.nocircle.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.nocircle.app.pages.account.login.LoginPage
import com.nocircle.app.pages.account.register.RegisterPage
import com.nocircle.app.pages.guide.GuidePage
import com.nocircle.app.pages.main.MainPage
import com.nocircle.common.expends.noComposable
import kotlinx.serialization.Serializable

@Composable
fun NoNavHost() {
	val navController = LocalNavController.current
	NavHost(navController, NoRoutes.Guide) {
		noComposable<NoRoutes.Guide> {
			GuidePage()
		}
		
		noComposable<NoRoutes.AccountLogin> {
			LoginPage()
		}
		
		noComposable<NoRoutes.AccountRegister> {
			RegisterPage()
		}
		
		noComposable<NoRoutes.Main> {
			MainPage()
		}
	}
}

object NoRoutes {
	
	@Serializable
	data object Guide
	
	@Serializable
	data object AccountLogin
	
	@Serializable
	data object AccountRegister
	
	@Serializable
	data object Main
}