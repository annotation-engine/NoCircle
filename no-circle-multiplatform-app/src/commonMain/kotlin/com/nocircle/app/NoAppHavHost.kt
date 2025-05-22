package com.nocircle.app

import androidx.compose.runtime.Composable
import com.nocircle.app.pages.account.login.LoginPage
import com.nocircle.app.pages.account.login.LoginRoute
import com.nocircle.app.pages.account.register.RegisterPage
import com.nocircle.app.pages.account.register.RegisterRoute
import com.nocircle.app.pages.guide.GuidePage
import com.nocircle.app.pages.guide.GuideRoute
import com.nocircle.app.pages.main.MainPage
import com.nocircle.app.pages.main.MainRoute
import com.nocircle.common.navigation.NoNavHost
import com.nocircle.common.navigation.NoNavHostController
import com.nocircle.common.navigation.rememberNoNavController

@Composable
fun NoAppNavHost() {
	val navController = NoNavControllerManager[NavRoot]
	NoNavHost(
		navController = navController,
		startDestination = GuideRoute
	) {
		composable<GuideRoute> { GuidePage() }
		composable<LoginRoute> { LoginPage() }
		composable<RegisterRoute> { RegisterPage() }
		composable<MainRoute> { MainPage() }
	}
}

object NoNavControllerManager {
	
	private val controllers = mutableMapOf<NavControllerKey, NoNavHostController>()
	
	@Composable
	operator fun get(key: NavControllerKey): NoNavHostController {
		return controllers.getOrPut(key) {
			rememberNoNavController()
		}
	}
	
	operator fun minusAssign(key: NavControllerKey) {
		controllers -= key
	}
}

interface NavControllerKey

data object NavRoot : NavControllerKey